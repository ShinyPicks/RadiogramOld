package io.shiny.telega

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Scene
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import io.shiny.telega.Persist.client
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi.*

class AuthActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }
    private fun onAuthorizationStateUpdated(authorizationState: AuthorizationState?) {
        if (authorizationState != null) {
            Persist.authorizationState = authorizationState
        }
        when (Persist.authorizationState.constructor) {
            AuthorizationStateWaitTdlibParameters.CONSTRUCTOR -> {
                val parameters = TdlibParameters()
                parameters.databaseDirectory = "tdlib"
                parameters.useMessageDatabase = true
                parameters.useSecretChats = true
                parameters.apiId = 94575
                parameters.apiHash = "a3406de8d171bb422bb6ddf3bbd800e2"
                parameters.systemLanguageCode = "en"
                parameters.deviceModel = Build.MODEL
                parameters.applicationVersion = "dev"
                parameters.enableStorageOptimizer = true
                client.send(SetTdlibParameters(parameters), AuthorizationRequestHandler())
            }
            AuthorizationStateWaitEncryptionKey.CONSTRUCTOR -> client.send(
                CheckDatabaseEncryptionKey(),
                AuthorizationRequestHandler()
            )
            AuthorizationStateWaitPhoneNumber.CONSTRUCTOR -> {
                //val phoneNumber: String = promptString("Please enter phone number: ")
                goTo(R.layout.auth_phone)
                client.send(
                    SetAuthenticationPhoneNumber(phoneNumber, null),
                    AuthorizationRequestHandler()
                )
            }
            AuthorizationStateWaitOtherDeviceConfirmation.CONSTRUCTOR -> {
                val link =
                    (Persist.authorizationState as AuthorizationStateWaitOtherDeviceConfirmation).link
                println("Please confirm this login link on another device: $link")
            }
            AuthorizationStateWaitCode.CONSTRUCTOR -> {
                val code: String = promptString("Please enter authentication code: ")
                client.send(CheckAuthenticationCode(code), AuthorizationRequestHandler())
            }
            AuthorizationStateWaitRegistration.CONSTRUCTOR -> {
                val firstName: String = promptString("Please enter your first name: ")
                val lastName: String = promptString("Please enter your last name: ")
                client.send(
                    RegisterUser(firstName, lastName),
                    AuthorizationRequestHandler()
                )
            }
            AuthorizationStateWaitPassword.CONSTRUCTOR -> {
                val password: String = promptString("Please enter password: ")
                client.send(
                    CheckAuthenticationPassword(password),
                    AuthorizationRequestHandler()
                )
            }
            AuthorizationStateReady.CONSTRUCTOR -> {
                Persist.hasAuth = true
            }
            AuthorizationStateLoggingOut.CONSTRUCTOR -> {
                Persist.hasAuth = false
                print("Logging out")
            }
            AuthorizationStateClosing.CONSTRUCTOR -> {
                Persist.hasAuth = false
                print("Closing")
            }
            AuthorizationStateClosed.CONSTRUCTOR -> {
                print("Closed")
                if (!needQuit) {
                    client = Client.create(
                        UpdateHandler(),
                        null,
                        null
                    ) // recreate client after previous has closed
                }
            }
            else -> Log.e("Radiogram", "Unsupported authorization state:" + Persist.authorizationState)
        }
    }

    private class AuthorizationRequestHandler : Client.ResultHandler {
        override fun onResult(`object`: Object) {
            when (`object`.constructor) {
                Error.CONSTRUCTOR -> Log.e("Radiogram", "Receive an error:$`object`")
                Ok.CONSTRUCTOR -> {
                }
                else -> Log.e("Radiogram", "Receive wrong response:$`object`")
            }
        }
    }

    fun goTo(rid: Int) {
        val sceneRoot: ViewGroup = findViewById(R.id.scene_root)
        val anotherScene: Scene = Scene.getSceneForLayout(sceneRoot, rid, this)
        var transition: Transition = Slide(Gravity.LEFT)
        TransitionManager.go(anotherScene, transition)
    }
}