package com.example.roommate.controllers.admin;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import com.example.roommate.application.service.BookingService;
import com.example.roommate.application.service.RoomService;
import com.example.roommate.application.service.MixService;
import com.example.roommate.application.service.UserService;
import com.example.roommate.application.validation.BookingsForm;
import com.example.roommate.application.validation.RoomForm;
import com.example.roommate.application.validation.UserForm;
import com.example.roommate.config.annotation.AdminOnly;
import com.example.roommate.domain.model.roomAggregat.Room;
import com.example.roommate.domain.model.userAggregat.Person;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@SuppressFBWarnings("EI_EXPOSE_REP2")
@Controller
@RequestMapping("/adminHome")
@AdminOnly
public class AdminController {

    /**
     * Attribut für dependency injection.
     */
    private  final RoomService roomService;

    /**
     * Attribut für dependency injection.
     */
    private final UserService userService;

    /**
     * Attribut für dependency injection.
     */
    private final BookingService bookingService;

    /**
     * Attribut für dependency injection.
     */
    private final MixService mixService;

    /**
     * Konstruktor der Klasse.
     * @param serviceRoom Instanz
     * @param serviceUser Instanz
     * @param serviceBooking Instanz
     * @param serviceMix Instanz
     */
    public AdminController(
            final RoomService serviceRoom,
            final UserService serviceUser,
            final BookingService serviceBooking,
            final MixService serviceMix) {
        this.roomService = serviceRoom;
        this.userService = serviceUser;
        this.bookingService = serviceBooking;
        this.mixService = serviceMix;
    }

    /**
     * Attribut der Model.
     * @param oAuth2User enthält informationen von GitHub
     * @return username
     */
    @ModelAttribute("admin")
    public String username(
            final @AuthenticationPrincipal OAuth2User oAuth2User) {
        return oAuth2User.getAttribute("login");
    }

    /**
     * Bearbeitet die Rendering der adminHome.
     * @param model leitet die Informationen in der HtlM-Code.
     * @param admin login Info
     * @return adminHome Seite
     */
    @GetMapping
    public String adminHome(
            final Model model,
            final @ModelAttribute("admin") String admin) {
        userService.addUserIfNotPresent(admin);
        model.addAttribute("admin", userService.greetUser(admin));
        model.addAttribute("rooms", roomService.getAllRooms());
        return "adminHome";
    }

    /**
     * Bearbeitet die Request von adminHome.
     * @param roomForm Room Objekt
     * @param redirectAttributes um daten weiter zuleiten
     * @return adminHome Seite
     */
    @PostMapping
    public String editRoomName(
            final RoomForm roomForm,
            final RedirectAttributes redirectAttributes) {
        if (roomService.isRoomNameIncorrect(roomForm.roomName())) {
            error4(redirectAttributes, "Room Name",
                    "Name already exists or empty");
            return "redirect:/adminHome";
        }
        success1(redirectAttributes, "Room Name", "updated");
        roomService.editRoom(roomForm.roomId(), roomForm.roomName());
        return "redirect:/adminHome";
    }

    /**
     * .
     * @param model leitet die Informationen in der HTML-Code
     * @param admin login Info
     * @return adminProfil
     */
    @GetMapping("/adminProfil")
    public String adminProfil(
            final Model model,
            final @ModelAttribute("admin") String admin) {
        if (userService.isUserPresent(admin)) {
            Person person = userService.findUserByUsername(admin);
            UserForm userForm = new UserForm(person.getTitle(),
                    person.getVorname(), person.getNachname());
            model.addAttribute("userForm", userForm);
        }
        return "adminProfil";
    }

    /**
     * .
     * @param admin login Info
     * @param userForm Objekt der Validierung
     * @param bindingResult um Fehler weiter zuleiten
     * @return adminHome
     */
    @PostMapping("/adminProfil")
    public String postAdminProfil(
            final @ModelAttribute("admin") String admin,
            final @Valid UserForm userForm,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "adminProfil";
        }
        userService.modifyUser(userForm.title(), admin,
                    userForm.firstName(), userForm.surName());
        return "redirect:/adminHome";
    }

    /**
     * .
     * @param model leitet die Informationen in der HtlM-Code
     * @return allBooking
     */
    @GetMapping("/allBooking")
    public String allBooking(
            final Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        model.addAttribute("userNames", userService.getAllUserName());
        model.addAttribute("filter", true);
        return "allBooking";
    }

    /**
     * .
     * @param bookingsForm        Objekt für die Validierung
     * @param redirectAttributes  um daten weiter zuleiten
     * @return userBooking Seite
     */

    @PostMapping("/allBooking/adminUpdateUserBooking")
    public String postUpdateUserBooking(
            final BookingsForm bookingsForm,
            final RedirectAttributes redirectAttributes) {
        if (bookingsForm.validation()) {
            error1(redirectAttributes);
            return "redirect:/adminHome/allBooking";
        }
        if (bookingService.isBookingCollisionFreeForUpdate(
                bookingsForm.bookingId(), bookingsForm.bookingForRoom(),
                bookingsForm.bookingForWorkplace(), bookingsForm.date(),
                bookingsForm.startTime(), bookingsForm.endTime())) {
            error4(redirectAttributes, "Booking",
                    "because collision with another Booking");
            return "redirect:/adminHome/allBooking";
        }
        bookingService.updateBooking(
                bookingsForm.bookingId(), bookingsForm.date(),
                bookingsForm.startTime(), bookingsForm.endTime());
        success1(redirectAttributes, "Booking", "update");
        return "redirect:/adminHome/allBooking";
    }

    /**
     * .
     * find All Bookings for Username
     * @param model  leitet die Informationen in der HtlM-Code.
     * @param userName login info
     * @return  allBooking Site
     */
    @GetMapping("/allBooking/findUserBooking")
    public String findUserBooking(
            final Model model,
            final String userName) {
            model.addAttribute("bookings",
                    bookingService.getBookingsFromUser(userName));
            model.addAttribute("reset", true);
            model.addAttribute("filter", false);
        return "allBooking";
    }

    /**
     * .
     *
     * @param bookingToDeleteID  Schlüssel
     * @param redirectAttributes um daten weiter zuleiten
     * @return userBooking Seite
     */
    @PostMapping("/allBooking/adminDeleteBooking")
    public String deleteUserBooking(
            final Long bookingToDeleteID,
            final RedirectAttributes redirectAttributes) {
        bookingService.deleteBooking(bookingToDeleteID);
        success1(redirectAttributes, "Booking", "deleted");
        return "redirect:/adminHome/allBooking";
    }

    /**
     * .
     * @param model leitet die Informationen in der HtlM-Code.
     * @param roomId Nummer des Platzes
     * @return adminRoomDetails
     */
    @GetMapping("/adminRoomDetails")
    public String adminRoomDetails(
            final Model model,
            final Long roomId
    ) {
        model.addAttribute("room", roomService.findRoomById(roomId));
        return "adminRoomDetails";
    }

    /**
     * .
     * @param roomId Nummer des Platzes
     * @param bookingsForm Nummer des Platzes
     * @param admin Nummer des Platzes
     * @param redirectAttributes um daten weiter zuleiten
     * @return adminRoomDetails
     */
    @PostMapping("/adminRoomDetails")
    public String adminRoomDetail(
            final Long roomId,
            final BookingsForm bookingsForm,
             final @ModelAttribute("admin") String admin,
            final RedirectAttributes redirectAttributes
    ) {
        if (bookingsForm.validation()) {
            error1(redirectAttributes);
            return "redirect:/adminHome/adminRoomDetails?roomId=" + roomId;
        }
        Room room = roomService.findRoomById(roomId);
        String message = mixService.notValidBooking(
                room.getWorkplaces(), bookingsForm.bookingForWorkplace(),
                room.getRoomName(), bookingsForm.date(),
                bookingsForm.startTime(), bookingsForm.endTime()
        );
        if (message != null) {
            error3(redirectAttributes, message);
            return "redirect:/adminHome/adminRoomDetails?roomId=" + roomId;
        }
        bookingService.doAddBooking(admin, bookingsForm.date(),
                bookingsForm.startTime(), bookingsForm.endTime(),
                bookingsForm.bookingForWorkplace(), room.getRoomName());
        success1(redirectAttributes, "Workplace", "blocked");
        return "redirect:/adminHome/adminRoomDetails?roomId=" + roomId;
    }


    /**
     *
     * @param model leitet die Informationen in der HtlM-Code.
     * @param roomForm Raum Objekt
     * @return editWorkplace
     */
    @GetMapping("/adminRoomDetails/editWorkplace")
    public String editWorkplace(
            final Model model,
            final RoomForm roomForm
    ) {
        model.addAttribute("room",
                roomService.findRoomById(roomForm.roomId()));
        model.addAttribute("workplaceNumber", roomForm.workplaceNumber());
        model.addAttribute("workplaceStatus", roomForm.workplaceStatus());
        model.addAttribute("equipmentDescription",
                roomForm.equipmentDescription());
        return "editWorkplace";
    }

    /**
     * .
     * @param roomForm Raum Objekt
     * @param redirectAttributes um daten weiter zuleiten
     * @return editWorkplace
     */
    @PostMapping("/adminRoomDetails/editWorkplace")
    public String postEditWorkplace(
            final RoomForm roomForm,
            final RedirectAttributes redirectAttributes
    ) {
        if (!roomService.isContainsCorrect(roomForm.equipmentDescription())) {
            error4(redirectAttributes, "Workplace",
                    "because Equipment is Empty");
            return "redirect:"
                    +
                    "/adminHome/adminRoomDetails/editWorkplace?workplaceNumber="
                    + roomForm.workplaceNumber()
                    + "&equipmentDescription="
                    + roomForm.equipmentDescription()
                    + "&roomID="
                    + roomForm.roomId();
        }
        roomService.editWorkplace(
                roomForm.workplaceNumber(), roomForm.workplaceStatus(),
                roomForm.roomId(), roomForm.equipmentDescription());
        success1(redirectAttributes, "Workplace", "updated");
        return "redirect:/adminHome/adminRoomDetails?roomId="
                + roomForm.roomId();
    }

    /**
     * .
     * @param redirectAttributes um daten weiter zuleiten
     * @param roomForm Raum Objekt
     * @return adminRoomDetails
     */
    @PostMapping("/adminRoomDetails/addNewWorkplace")
    public String addNewWorkplace(
            final RoomForm roomForm,
            final RedirectAttributes redirectAttributes
    ) {
        if (!roomService.isWorkplaceToaddCorrect(
                roomForm.roomId(), roomForm.workplaceNumber(),
                roomForm.equipmentDescription()
        )) {
            error5(redirectAttributes);
            return "redirect:/adminHome/adminRoomDetails?roomId="
                    + roomForm.roomId();
        }
        roomService.addWorkplaceForRoom(
                roomForm.workplaceNumber(), roomForm.equipmentDescription(),
                roomForm.roomId());
        success1(redirectAttributes, "Workplace", "added");
        return "redirect:/adminHome/adminRoomDetails?roomId="
                + roomForm.roomId();
    }

    /**
     * .
     * @param admin login Info
     * @param roomName der Name des Rooms
     * @param redirectAttributes um daten weiter zuleiten
     * @return adminHome
     */
    @PostMapping("/addNewRoom")
    public String addNewRoom(
            final @ModelAttribute("admin") String admin,
            final String roomName,
            final RedirectAttributes redirectAttributes) {
        if (roomService.isRoomNameIncorrect(roomName)) {
            error2(redirectAttributes);
            return "redirect:/adminHome";
        }
        roomService.addNewRoom(roomName);
        success1(redirectAttributes, "New Room", "added");
        return "redirect:/adminHome";
    }

    /**
     * .
     * @param roomToDeleteID Schlüssel
     * @param redirectAttributes um daten weiter zuleiten
     * @return adminHome
     */
    @PostMapping("/deleteRoom")
    public String deletingRoom(
            final Long roomToDeleteID,
            final RedirectAttributes redirectAttributes
    ) {
        roomService.deleteRoom(roomToDeleteID);
        success1(redirectAttributes, "Room", "deleted");
        return "redirect:/adminHome";
    }

    /**
     * .
     * @param roomID Schlüssel
     * @param workplaceNumber Nummer des Platzes
     * @param redirectAttributes um daten weiter zuleiten
     * @return adminHome
     */
    @PostMapping("/adminRoomDetails/deleteWorkplace")
    public String deletingWorkplace(
            final Long roomID,
            final int workplaceNumber,
            final RedirectAttributes redirectAttributes
    ) {
        roomService.deletingWorkplace(roomID, workplaceNumber);
        success1(redirectAttributes, "Workplace", "deleted");
        return "redirect:/adminHome/adminRoomDetails?roomID="
                + roomID;
    }

    /**
     * .
     * @param redirectAttributes um daten weiter zuleiten
     * @param entity variable
     * @param status variable
     */
    private void success1(
            final RedirectAttributes redirectAttributes,
            final String entity,
            final String status) {
        redirectAttributes.addFlashAttribute("success",
                entity + " has been successfully " + status + "!!!");
    }

    private void error1(
            final RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error",
                BookingsForm.getErrorMessage());
    }

    private void error2(final RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error",
                "Room Name already exists or empty!!!");
    }

    private void error3(
            final RedirectAttributes redirectAttributes,
            final String message) {
        redirectAttributes.addFlashAttribute("error",
                message);
    }

    private void error4(
            final RedirectAttributes redirectAttributes,
            final String entity,
            final String message) {
        redirectAttributes.addFlashAttribute("error",
                "Unable to update " + entity
                        +
                        ", " + message + "!!!");
    }

    private void error5(
            final RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error",
                "Unable to add new Workplace, "
                        +
                        "because Equipments is Empty "
                        +
                        "or Workplace Number is already exist!!!");
    }
}
