module sensitive.service.app {
    requires sensitive.service.api;
    exports com.infamous.framework.sensitive.service;
    opens com.infamous.framework.sensitive.service;
}