package io.shiny.telega

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.transition.ChangeBounds
import androidx.transition.Scene
import androidx.transition.Transition
import androidx.transition.TransitionManager
import android.view.ViewGroup

class TransitionTest : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition_test)
        trans()
    }

    fun trans() {
        val sceneRoot: ViewGroup = findViewById(R.id.scene_root)
        val aScene: Scene = Scene.getSceneForLayout(sceneRoot, R.layout.auth_hello, this)
        val anotherScene: Scene = Scene.getSceneForLayout(sceneRoot, R.layout.auth_wait, this)
        var transition: Transition = ChangeBounds()
        TransitionManager.go(anotherScene, transition)
    }
}