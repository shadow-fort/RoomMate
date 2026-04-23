package com.example.roommate.controllers.user;


import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import com.example.roommate.application.service.BookingService;
import com.example.roommate.application.service.RoomService;
import com.example.roommate.application.service.MixService;
import com.example.roommate.application.service.UserService;
import com.example.roommate.application.validation.BookingsForm;
import com.example.roommate.application.validation.RoomForm;
import com.example.roommate.application.validation.UserForm;
import com.example.roommate.domain.model.roomAggregat.Room;
import com.example.roommate.domain.model.userAggregat.Person;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.GrantedAuthority;
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

import java.util.Set;
import java.util.stream.Collectors;


@SuppressFBWarnings("EI_EXPOSE_REP2")
@RequestMapping("/userHome")
@Controller
public class UserController {

    /**
     * Attribut für dependency injection.
     */
    private final RoomService roomService;

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
     *
     * @param serviceRoom    Instanz
     * @param serviceUser    Instanz
     * @param serviceBooking Instanz
     * @param serviceMix Instanz
     */
    public UserController(
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
     *
     * @param oAuth2User enthält informationen von GitHub
     * @return username
     */
    @ModelAttribute("user")
    public String username(
            final @AuthenticationPrincipal OAuth2User oAuth2User) {
        return oAuth2User.getAttribute("login");
    }

    /**
     * .
     * @param oAuth2User enthält informationen von GitHub
     * @return eine Menge, die Role enthält
     */
    @ModelAttribute("roles")
    public Set<String> rol(
            final @AuthenticationPrincipal OAuth2User oAuth2User) {
        return oAuth2User.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }


    /**
     * Bearbeitet die Rendering der userHome.
     *
     * @param model               leitet die Informationen in der HtlM-Code.
     * @param user                login Info
     * @param roles                login Info
     * @param selectedDescription Equipment des Platzes
     * @return userHome Seite
     */
    @GetMapping
    public String userHome(
            final Model model,
            final @ModelAttribute("user") String user,
            final @ModelAttribute("roles") Set<String> roles,
            final @Param("selectedDescription") String selectedDescription) {
        userService.addUserIfNotPresent(user);
        model.addAttribute("userInf", roles);
        model.addAttribute("user", userService.greetUser(user));
        model.addAttribute("rooms",
                roomService.getAllRooms(selectedDescription));
        model.addAttribute("descriptions",
                roomService.getAllEquipmentsDescription());
        return "userHome";
    }

    /**
     * .
     *
     * @param model               leitet die Informationen in der HtlM-Code.
     * @param bookingsForm variable
     * @param selectedDescription variable
     * @param redirectAttributes um daten weiter zuleiten
     * @return userHome Seite
     */
    @GetMapping("/userHomeWithSearchCriteria")
    public String userHomeWithEquipmentsSearchCriteria(
            final Model model,
            final BookingsForm bookingsForm,
            final @Param("selectedDescription") String selectedDescription,
            final RedirectAttributes redirectAttributes) {
        if (bookingsForm.validation()) {
            error1(redirectAttributes);
            return "redirect:/userHome";
        }
        model.addAttribute("selectedDescription", selectedDescription);
        model.addAttribute("description",
                MixService.getDescription(bookingsForm.date(),
                        bookingsForm.startTime(), bookingsForm.endTime()));
        model.addAttribute("rooms",
                mixService.getAllFreeRoom(
                        bookingsForm.date(), bookingsForm.startTime(),
                        bookingsForm.endTime(), selectedDescription));
        return "userHomeWithSearchCriteria";
    }


    /**
     * .
     * @param roomId Schlüssel
     * @param selectedDescription Equipments des Platzes
     * @param bookingsForm Objekt für die Validierung
     * @param user login Info
     * @param redirectAttributes um daten weiter zuleiten
     * @return userHome
     */
    @PostMapping("/userHomeWithSearchCriteria")
    public String userHomeWithEquipmentsSearchCriteria(
            final Long roomId,
            final String selectedDescription,
            final BookingsForm bookingsForm,
            final @ModelAttribute("user") String user,
            final RedirectAttributes redirectAttributes) {
        System.out.println(bookingsForm);
        if (bookingsForm.validation()) {
            error1(redirectAttributes);
            return "redirect:"
                    +
                    "/userHome/userHomeWithSearchCriteria?selectedDescription="
                    + selectedDescription;
        }
        Room room = roomService.findRoomById(roomId);
        String message = mixService.notValidBooking(
                room.getWorkplaces(), bookingsForm.bookingForWorkplace(),
                room.getRoomName(), bookingsForm.date(),
                bookingsForm.startTime(), bookingsForm.endTime()
        );
        if (message != null) {
            error3(redirectAttributes, message);
            return "redirect:"
                    +
                    "/userHome/userHomeWithSearchCriteria?selectedDescription="
                    + selectedDescription;
        }
        bookingService.doAddBooking(user, bookingsForm.date(),
                bookingsForm.startTime(), bookingsForm.endTime(),
                bookingsForm.bookingForWorkplace(), room.getRoomName());
        success1(redirectAttributes, "added");
        return "redirect:/userHome";
    }

    /**
     * .
     * @param roomId              Schlüssel
     * @param model               leitet die Informationen in der HtlM-Code
     * @param bookingsForm        Objekt für die Validierung
     * @param selectedDescription Equipment des Platzes
     * @return userRoomDetails Seite
     */
    @GetMapping("/userRoomDetails")
    public String userRoomDetailsSite(
            final Long roomId,
            final Model model,
            final BookingsForm bookingsForm,
            final String selectedDescription) {
        model.addAttribute("room",
                mixService.searchForRoom(
                        roomId, selectedDescription, bookingsForm.date(),
                        bookingsForm.startTime(), bookingsForm.endTime()
                ));
        model.addAttribute("descriptions",
                roomService.getRoomWithEquipmentsDescription(roomId));
        return "userRoomDetails";
    }

    /**
     * .
     * @param model leitet die Informationen in der HtlM-Code
     * @param roomForm Raum Objekt
     * @return workplaceStatus
     */
    @GetMapping("/userRoomDetails/workplaceStatus")
    public String workplaceStatus(
            final Model model,
            final RoomForm roomForm
            ) {
        model.addAttribute("room",
                roomService.findRoomById(roomForm.roomId()));
        model.addAttribute("workplaceNumber",
                roomForm.workplaceNumber());
        model.addAttribute("workplaceStatus",
                roomForm.workplaceStatus());
        model.addAttribute("bookings",
                bookingService.findBookingByRoomNameAndWorkplaceNumber(
                        roomForm.roomName(), roomForm.workplaceNumber()));
        return "workplaceStatus";
    }

    /**
     * .
     * @param roomId              Schlüssel
     * @param bookingsForm        Objekt für die Validierung
     * @param user                login Info
     * @param redirectAttributes  um daten weiter zuleiten
     * @return userRoomDetails Seite
     */
    @PostMapping("/userRoomDetails")
    public String userRoomDetailsSeitePost(
            final Long roomId,
            final BookingsForm bookingsForm,
            final @ModelAttribute("user") String user,
            final RedirectAttributes redirectAttributes) {
        if (bookingsForm.validation()) {
            error1(redirectAttributes);
            return "redirect:/userHome/userRoomDetails?roomId=" + roomId;
        }
        Room room = roomService.findRoomById(roomId);
        String message = mixService.notValidBooking(
                room.getWorkplaces(), bookingsForm.bookingForWorkplace(),
                room.getRoomName(), bookingsForm.date(),
                bookingsForm.startTime(), bookingsForm.endTime()
        );
        if (message != null) {
            error3(redirectAttributes, message);
            return "redirect:/userHome/userRoomDetails?roomId=" + roomId;
        }
        bookingService.doAddBooking(user, bookingsForm.date(),
                bookingsForm.startTime(), bookingsForm.endTime(),
                bookingsForm.bookingForWorkplace(), room.getRoomName());
        success1(redirectAttributes, "added");
        return "redirect:/userHome/userRoomDetails?roomId=" + roomId;
    }

    /**
     * .
     * @param model leitet die Informationen in der HtlM-Code.
     * @param user  login Info
     * @return userBooking Seite
     */
    @GetMapping("/userBooking")
    public String userBookingSeite(
            final Model model,
            final @ModelAttribute("user") String user) {
        model.addAttribute("user", user);
        model.addAttribute("myBookings",
                bookingService.getBookingsFromUser(user));
        return "userBooking";
    }

    /**
     * .
     * @param bookingsForm        Objekt für die Validierung
     * @param redirectAttributes  um daten weiter zuleiten
     * @return userBooking Seite
     */

    @PostMapping("/userBooking/updateUserBooking")
    public String postUpdateUserBooking(
            final BookingsForm bookingsForm,
            final RedirectAttributes redirectAttributes) {
        if (bookingsForm.validation()) {
            error1(redirectAttributes);
            return "redirect:/userHome/userBooking";
        }
        if (bookingService.isBookingCollisionFreeForUpdate(
                bookingsForm.bookingId(), bookingsForm.bookingForRoom(),
                bookingsForm.bookingForWorkplace(), bookingsForm.date(),
                bookingsForm.startTime(), bookingsForm.endTime())) {
           error2(redirectAttributes);
            return "redirect:/userHome/userBooking";
        }
        bookingService.updateBooking(
                bookingsForm.bookingId(), bookingsForm.date(),
                bookingsForm.startTime(), bookingsForm.endTime());
        success1(redirectAttributes, "updated");
        return "redirect:/userHome/userBooking";
    }

    /**
     * .
     *
     * @param bookingToDeleteID  Schlüssel
     * @param redirectAttributes um daten weiter zuleiten
     * @return userBooking Seite
     */
    @PostMapping("userBooking/deleteBooking")
    public String deleteUserBooking(
            final Long bookingToDeleteID,
            final RedirectAttributes redirectAttributes) {
        bookingService.deleteBooking(bookingToDeleteID);
        success1(redirectAttributes, "deleted");
        return "redirect:/userHome/userBooking";
    }

    /**
     * .
     *
     * @param model    leitet die Informationen in der HtlM-Code.
     * @param userForm Objekt für die Validierung
     * @param user     login Info
     * @return userProfil Seite
     */
    @GetMapping("/userProfil")
    public String userProfil(
            final Model model,
            final UserForm userForm,
            final @ModelAttribute("user") String user) {
        if (userService.isUserPresent(user)) {
            Person person = userService.findUserByUsername(user);
            UserForm userForm1 = new UserForm(person.getTitle(),
                    person.getVorname(), person.getNachname());
            model.addAttribute("userForm", userForm1);
            return "userProfil";
        }
        model.addAttribute("userForm", userForm);
        return "userProfil";
    }

    /**
     * Behandel das Editing ein User.
     *
     * @param user               von GitHub
     * @param userForm           Für die Validierung
     * @param bindingResult      um Fehler weiter zuleiten
     * @return ein redirect auf den userHome Seite
     */
    @PostMapping("/userProfil")
    public String postUserProfil(
            final @Valid UserForm userForm,
            final BindingResult bindingResult,
            final @ModelAttribute("user") String user) {
        if (bindingResult.hasErrors()) {
            return "userProfil";
        }
        userService.modifyUser(
                userForm.title(), user, userForm.firstName(),
                userForm.surName());
        return "redirect:/userHome";
    }

    private void success1(
            final RedirectAttributes redirectAttributes,
            final String status) {
        redirectAttributes.addFlashAttribute("success",
                "Booking has been successfully " + status + "!!!");
    }

    private void error1(
            final RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error",
                BookingsForm.getErrorMessage());
    }

    private void error2(final RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error",
                "Unable to update Booking, "
                        +
                        "because collision with another Booking!!! ");
    }

    private void error3(
            final RedirectAttributes redirectAttributes,
            final String message) {
        redirectAttributes.addFlashAttribute("error",
                message);
    }
}