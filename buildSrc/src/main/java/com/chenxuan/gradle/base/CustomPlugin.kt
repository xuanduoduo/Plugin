package com.chenxuan.gradle.base

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.chenxuan.gradle.catch.CatchTransform
import com.chenxuan.gradle.image.ImageTransform
import com.chenxuan.gradle.sample.SampleTransform
import com.chenxuan.gradle.thread.ThreadTransform
import com.chenxuan.gradle.track.TrackTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

class CustomPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val isApp = project.plugins.hasPlugin(AppPlugin::class.java)
        if (isApp) {
            project.extensions.findByType(BaseExtension::class.java)?.run {
                registerTransform(ThreadTransform())
                registerTransform(ImageTransform())
                registerTransform(TrackTransform())
                registerTransform(CatchTransform())
                registerTransform(SampleTransform())
            }
        }
    }
}