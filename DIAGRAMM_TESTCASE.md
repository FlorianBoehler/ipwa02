```mermaid
classDiagram
    class TestCase {
        -Long id
        -String customId
        -String title
        -String description
        -String prerequisites
        -String expectedResult
        -Long assignedUserId
        +TestCase()
        +TestCase(title, description, prerequisites, expectedResult, requirement)
        +getId() Long
        +setId(Long id) void
        +getCustomId() String
        +setCustomId(String customId) void
        +getTitle() String
        +setTitle(String title) void
        +getDescription() String
        +setDescription(String description) void
        +getPrerequisites() String
        +setPrerequisites(String prerequisites) void
        +getExpectedResult() String
        +setExpectedResult(String expectedResult) void
        +getRequirement() Requirement
        +setRequirement(Requirement requirement) void
        +getAssignedUser() User
        +setAssignedUser(User assignedUser) void
        +getTestRun() TestRun
        +setTestRun(TestRun testRun) void
        +getAssignedUserId() Long
        +setAssignedUserId(Long id) void
    }

    class TestCaseService {
        <<interface>>
        +createTestCase(TestCase testCase) void
        +getTestCase(Long id) TestCase
        +getAllTestCases() List~TestCase~
        +getTestCasesByTester(User tester) List~TestCase~
        +updateTestCase(TestCase testCase) void
        +deleteTestCase(Long id) void
        +hasRelatedTestResults(Long testCaseId) boolean
        +getAllTestCasesNotAssignedToTestRun() List~TestCase~
    }

    class TestCaseServiceImpl {
        -EntityManager em
        -long lastUsedNumber
        +initializeLastUsedNumber() void
        -generateCustomId() String
    }

    class TestCaseBean {
        -TestCaseService testCaseService
        -RequirementService requirementService
        -UserService userService
        -TestRunService testRunService
        -LoginBean loginBean
        -PageAccessBean pageAccessBean
        -FacesContext facesContext
        -Long id
        -String title
        -String description
        -String prerequisites
        -String expectedResult
        -Long selectedRequirementId
        -Long selectedUserId
        -List~TestCase~ allTestCases
        -List~TestCase~ filteredTestCases
        -Long testRunId
        +init() void
        +loadAllTestCases() void
        +loadFilteredTestCases() void
        +saveOrUpdateTestCase() void
        +editTestCase(TestCase testCase) void
        +deleteTestCase(TestCase testCase) void
        +getRequirementOptions() List~SelectItem~
        +clearFields() String
        +getUserOptions() List~SelectItem~
    }

    class Requirement {
        -Long id
        -String title
    }

    class User {
        -Long id
        -String username
        -UserRole role
    }

    class TestRun {
        -Long id
        -String name
    }

    class TestCaseJSF {
        <<view>>
        Form
        DataTable
        InputFields
        CommandButtons
    }

    class TestRunJSF {
        <<view>>
        Form
        DataTable
        InputFields
        CommandButtons
    }

    class TestResultJSF {
        <<view>>
        Form
        DataTable
        InputFields
        CommandButtons
    }

    TestCase "1" --> "1" Requirement : has
    TestCase "1" --> "0..1" TestRun : belongs to
    TestCase "1" --> "0..1" User : assigned to
    TestCaseServiceImpl ..|> TestCaseService
    TestCaseServiceImpl --> TestCase : manages
    TestCaseBean --> TestCaseService : uses
    TestCaseBean --> RequirementService : uses
    TestCaseBean --> UserService : uses
    TestCaseBean --> TestRunService : uses
    TestCaseBean --> LoginBean : uses
    TestCaseBean --> PageAccessBean : uses
    TestCaseBean --> TestCase : manages
    TestCaseJSF  ..> TestCaseBean : uses
    TestRunJSF  ..> TestCaseBean : uses
    TestResultJSF  ..> TestCaseBean : uses