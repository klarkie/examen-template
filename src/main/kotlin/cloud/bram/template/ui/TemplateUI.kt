package cloud.bram.template.ui

import cloud.bram.template.domain.Role
import cloud.bram.template.security.SecurityUtils
import com.vaadin.navigator.PushStateNavigation
import com.vaadin.navigator.ViewLeaveAction
import com.vaadin.server.ErrorHandler
import com.vaadin.server.VaadinRequest
import com.vaadin.spring.annotation.SpringUI
import com.vaadin.ui.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import java.net.URI

@SpringUI(path = "/app")
@PushStateNavigation
@Secured(Role.NORMAL, Role.ADMIN)
class TemplateUI : UI() {

    @set:Autowired
    lateinit var templateNavigator: TemplateNavigator

    companion object {
        val logger = LoggerFactory.getLogger(TemplateUI::class.java)
    }

    override fun init(request: VaadinRequest) {
//        navigator = templateNavigator
        content = VerticalLayout(
                Label("Hello, World!"),
                Label(SecurityUtils.getEmail() ?: "Guest"),
                Button("Click Me Daddy", Button.ClickListener {
                    Notification.show("I CLICKED MY DADDY", Notification.Type.TRAY_NOTIFICATION)
                }),
                Button("Sign Out", Button.ClickListener {
                    val logout = ViewLeaveAction {
                        session.session.invalidate()
                        page.location = URI("/")
                    }
                    access { templateNavigator.runAfterLeaveConfirmation(logout) }
                })
        )
        errorHandler = ErrorHandler { event ->
            val throwable = event.throwable
            Notification.show(throwable.message, Notification.Type.ERROR_MESSAGE)
            logger.error(throwable.message, throwable)
        }
    }
}