```mermaid
classDiagram
    class TestResultJSF {
        <<view>>
        Form
        DataTable
        InputFields
        CommandButtons
    }

    class TestResultBean {
        -TestResultService testResultService
        -TestCaseService testCaseService
        -TestCase currentTestCase
        -TestResult currentTestResult
        -Long id
        -TestResult.Status status
        -String result
        -String comment
        -LocalDateTime executionDate
        -Long filteredTestCaseId
        -LazyDataModel<TestResult> lazyModel
        +getLazyModel() LazyDataModel<TestResult>
        +saveOrUpdateTestResult()
        +clearFields()
        +editTestResult(TestCase)
        +deleteTestResult(TestResult)
        +getStatusOptions() List<SelectItem>
        +prepareNewTestResult(TestCase)
        +viewTestResultDetails(TestCase)
        +getCustomId() String
        +setCustomId(String)
        +getTestResultForTestCase(TestCase) TestResult
        +hasTestResult(TestCase) boolean
    }

    class TestResultService {
        <<interface>>
        +getTestResults(int, int, Map<String, SortMeta>, Map<String, FilterMeta>) List<TestResult>
        +countTestResults() int
        +createTestResult(TestResult)
        +getTestResult(Long) TestResult
        +getAllTestResults() List<TestResult>
        +updateTestResult(TestResult)
        +deleteTestResult(Long)
        +getTestResultForTestCase(TestCase) TestResult
        +hasTestResultForTestCase(TestCase) boolean
    }

    class TestResultServiceImpl {
        -EntityManager em
        -long lastUsedNumber
        +initializeLastUsedNumber()
        +generateCustomId() String
        +createTestResult(TestResult)
        +getTestResult(Long) TestResult
        +getAllTestResults() List<TestResult>
        +updateTestResult(TestResult)
        +deleteTestResult(Long)
        +getTestResults(int, int, Map<String, SortMeta>, Map<String, FilterMeta>) List<TestResult>
        +countTestResults() int
        +getTestResultForTestCase(TestCase) TestResult
        +hasTestResultForTestCase(TestCase) boolean
    }

    class TestResult {
        -Long id
        -String customId
        -TestCase testCase
        -Status status
        -String result
        -String comment
        -LocalDateTime executionDate
        +Status: NOT_STARTED, IN_PROGRESS, PASSED, FAILED
    }

    class TestCase {
        -Long id
        -String customId
        -String title
        -String description
    }

    TestResultJSF ..> TestResultBean : uses
    TestResultBean --> TestResultService : uses
    TestResultBean --> TestCaseService : uses
    TestResultServiceImpl ..|> TestResultService : implements
    TestResultServiceImpl --> EntityManager : uses
    TestResultServiceImpl --> TestResult : manages
    TestResultBean --> TestResult : uses
    TestResult "0..*" --> "1" TestCase : belongs to