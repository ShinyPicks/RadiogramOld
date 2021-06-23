package io.shiny.telega

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.*

class TransitionTest : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition_test)
    }

    fun trans(view: View) {
        val sceneRoot: ViewGroup = findViewById(R.id.scene_root)
        val aScene: Scene = Scene.getSceneForLayout(sceneRoot, R.layout.auth_hello, this)
        val anotherScene: Scene = Scene.getSceneForLayout(sceneRoot, R.layout.auth_wait, this)
        var transition: Transition = Slide(Gravity.LEFT)
        TransitionManager.go(anotherScene, transition)
    }
}