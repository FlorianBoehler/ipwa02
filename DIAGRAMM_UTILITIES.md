```mermaid
classDiagram
class LocalDateTimeConverter {
-DateTimeFormatter formatter
+getAsObject(FacesContext, UIComponent, String) Object
+getAsString(FacesContext, UIComponent, Object) String
}

    class JSFPage {
        <<interface>>
    }

    class TestResultJSF {
        <<view>>
    }

    class TestRunJSF {
        <<view>>
    }

    class DashboardJSF {
        <<view>>
    }

    class UIComponent {
        <<interface>>
    }

    class FacesContext {
        <<interface>>
    }

    class Converter {
        <<interface>>
    }

    class TestResult {
        -LocalDateTime executionDate
    }

    class TestRun {
        -LocalDateTime startDate
        -LocalDateTime endDate
    }

    LocalDateTimeConverter ..|> Converter
    LocalDateTimeConverter --> FacesContext : uses
    LocalDateTimeConverter --> UIComponent : uses
    JSFPage --> LocalDateTimeConverter : uses
    TestResultJSF --|> JSFPage
    TestRunJSF --|> JSFPage
    DashboardJSF --|> JSFPage
    TestResultJSF --> TestResult : displays
    TestRunJSF --> TestRun : displays
    TestResult --> LocalDateTime : contains
    TestRun --> LocalDateTime : contains