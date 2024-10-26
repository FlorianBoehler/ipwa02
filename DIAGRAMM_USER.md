```mermaid
classDiagram
    class LoginJSF {
        <<view>>
        LoginForm
        CreateAdminForm
    }

    class MenubarJSF {
        <<view>>
        MenuItems
        LogoutButton
    }

    class AccessDeniedJSF {
        <<view>>
        ErrorMessage
        BackToDashboardButton
    }

    class LoginBean {
        -UserService userService
        -FacesContext facesContext
        -String username
        -String password
        -User currentUser
        +login() String
        -redirectBasedOnRole(User.UserRole) String
        +logout() String
        +hasRole(String) boolean
        +getUsername() String
        +setUsername(String) void
        +getPassword() String
        +setPassword(String) void
        +getCurrentUser() User
    }

    class PageAccessBean {
        -LoginBean loginBean
        -Map<String, Object> accessRights
        +init()
        +hasAccessToPage(String) boolean
        +canViewAllTestCases() boolean
        +checkAccess(String)
        +shouldRenderMenuItem(String) boolean
    }

    class AuthorizationFilter {
        -LoginBean loginBean
        +doFilter(ServletRequest, ServletResponse, FilterChain)
        -hasAccess(String, User) boolean
    }

    class UserService {
        <<interface>>
        +authenticate(String, String) User
        +createUser(User, String) User
        +updateUser(User) User
        +getUserById(Long) User
        +getAllUsers() List<User>
        +getUserByUsername(String) User
    }

    class User {
        -Long id
        -boolean active
        -String username
        -String password
        -String email
        -UserRole role
        -List~TestCase~ assignedTestCases
        +User()
        +User(String username, String password, String email, UserRole role)
        +User(String username, String password, String email, UserRole role, boolean active)
        +User(User other)
        +equals(Object o) boolean
        +hashCode() int
        +toString() String
        +setPassword(String password) void
        +checkPassword(String password) boolean
        +getId() Long
        +setId(Long id) void
        +getUsername() String
        +setUsername(String username) void
        +getEmail() String
        +setEmail(String email) void
        +getRole() UserRole
        +setRole(UserRole role) void
        +isActive() boolean
        +setActive(boolean active) void
    }

    class UserRole {
        <<enumeration>>
        REQUIREMENTS_ENGINEER
        TEST_MANAGER
        TEST_CREATOR
        TESTER
        ADMIN
    }

    class PageAccessJSON {
        <<file>>
        pageAccess
        testCases
    }

    LoginJSF ..> LoginBean : uses
    MenubarJSF ..> LoginBean : uses
    MenubarJSF ..> PageAccessBean : uses
    LoginBean --> UserService : uses
    LoginBean --> User : manages
    PageAccessBean --> LoginBean : uses
    PageAccessBean ..> PageAccessJSON : reads
    AuthorizationFilter --> LoginBean : uses
    AuthorizationFilter --> User : checks
    UserService --> User : manages
    AuthorizationFilter ..> AccessDeniedJSF : redirects to
    PageAccessBean ..> AccessDeniedJSF : redirects to
    User --> UserRole : has