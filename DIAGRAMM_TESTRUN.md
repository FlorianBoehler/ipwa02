```mermaid
classDiagram
    class TestRunJSF {
        <<view>>
        Form
        DataTable
        InputFields
        CommandButtons
    }

    class TestRunBean {
        -TestRunService testRunService
        -UserService userService
        -TestCaseService testCaseService
        -TestRun testRun
        -List<TestRun> testRuns
        -List<TestCase> availableTestCases
        -List<TestCase> selectedTestCases
        -List<TestCase> unassignedTestCases
        +init()
        +loadTestRuns()
        +loadAvailableTestCases()
        +saveTestRun()
        +editTestRun(TestRun)
        +deleteTestRun(TestRun)
        +isTestCaseAlreadyAdded(TestCase) boolean
        +clearForm()
        +getStatusOptions() List<TestRun.TestRunStatus>
        +addTestCaseToTestRun(TestCase)
        +assignTesterToTestCase(TestCase)
        +removeTestCaseFromTestRun(TestCase)
    }

    class TestRunService {
        <<interface>>
        +saveTestRun(TestRun) TestRun
        +deleteTestRun(Long)
        +getTestRunById(Long) TestRun
        +getTestRunWithTestCases(Long) TestRun
        +getAllTestRuns() List<TestRun>
        +addTestCaseToTestRun(Long, Long) TestRun
        +removeTestCaseFromTestRun(Long, Long) TestRun
        +hasRelatedTestCases(Long) boolean
    }

    class TestRunServiceImpl {
        -EntityManager em
        -long lastUsedNumber
        +initializeLastUsedNumber()
        +generateCustomId() String
        +saveTestRun(TestRun) TestRun
        +deleteTestRun(Long)
        +getTestRunById(Long) TestRun
        +getTestRunWithTestCases(Long) TestRun
        +getAllTestRuns() List<TestRun>
        +addTestCaseToTestRun(Long, Long) TestRun
        +removeTestCaseFromTestRun(Long, Long) TestRun
        +hasRelatedTestCases(Long) boolean
    }

    class TestRun {
        -Long id
        -String customId
        -String name
        -String description
        -LocalDate startDate
        -LocalDate endDate
        -TestRunStatus status
        -Set<TestCase> testCases
        +TestRunStatus: PLANNED, IN_PROGRESS, COMPLETED
    }

    class TestCase {
        -Long id
        -String customId
        -String title
        -String description
    }

    TestRunJSF ..> TestRunBean : uses
    TestRunBean --> TestRunService : uses
    TestRunBean --> UserService : uses
    TestRunBean --> TestCaseService : uses
    TestRunServiceImpl ..|> TestRunService : implements
    TestRunServiceImpl --> EntityManager : uses
    TestRunServiceImpl --> TestRun : manages
    TestRunBean --> TestRun : uses
    TestRun "1" --> "0..*" TestCase : contains