package com.example.roommate.architecturTest;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.example.roommate.RoomMateApplication;
import com.example.roommate.config.annotation.AdminOnly;
import com.example.roommate.config.annotation.AggregateRoot;
import com.example.roommate.controllers.admin.AdminController;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
import static com.example.roommate.architekturTest.rules.HaveExactlyOneAggregateRoot.HAVE_EXACTLY_ONE_AGGREGATE_ROOT;


@AnalyzeClasses(packagesOf = RoomMateApplication.class, importOptions = ImportOption.DoNotIncludeTests.class)
public class OnionArchitectureTest {

    @ArchTest
    ArchRule alleKlasseInApplicationServiceDieMitServiceEndetSindMitServiceAnnotiert = classes()
            .that()
            .resideInAPackage("..application.service..")
            .and()
            .areAnnotatedWith(Service.class)
            .should()
            .haveSimpleNameEndingWith("Service");

    @ArchTest
    ArchRule variableInControllerKlasseMuessenImmerPrivateSein = fields()
            .that()
            .areDeclaredInClassesThat()
            .areAnnotatedWith(Controller.class)
            .should()
            .bePrivate();

    @ArchTest
    ArchRule nurControllerKlassenKoennenZugriffHabenAufAnderenControllerKlassenHaben = noClasses()
            .that()
            .areNotAnnotatedWith(Controller.class)
            .should()
            .dependOnClassesThat()
            .areAnnotatedWith(Controller.class);

    @ArchTest
    ArchRule alleKlassenInControllerPackageEndetMitController = classes()
            .that()
            .resideInAPackage("..controllers..")
            .and()
            .areAnnotatedWith(Controller.class)
            .should()
            .haveSimpleNameEndingWith("Controller");

    @ArchTest
    ArchRule nurAggregateRootIstPublic = classes()
            .that()
            .areNotAnnotatedWith(AggregateRoot.class)
            .and()
            .resideInAPackage("..domain.model..")
            .should()
            .notBePublic()
            .orShould()
            .beRecords()
            .because("the implementation of an aggregate "
                    +
                    "should be hidden");

    @ArchTest
    ArchRule nurEinAggregateRootProPackageDieEndetMitAggregate = slices()
            .matching("..domain.model.(*)..")
            .should(HAVE_EXACTLY_ONE_AGGREGATE_ROOT);


    @ArchTest
    ArchRule layerTest = layeredArchitecture().withOptionalLayers(true)
            .layer("controller").definedBy("..controllers..")
            .layer("model").definedBy("..model..")
            .layer("service").definedBy("..service..")
            .layer("repository").definedBy("..database..")
            .whereLayer("controller")
            .mayNotBeAccessedByAnyLayer();

    @ArchTest
    ArchRule onionTest = onionArchitecture().withOptionalLayers(true)
            .domainModels("..domain.model..")
            .domainServices("..domain.service..")
            .applicationServices("..application.service..")
            .adapter("web","..controllers..")
            .adapter("db", "..database..")
            .adapter("config", "..config..");


}