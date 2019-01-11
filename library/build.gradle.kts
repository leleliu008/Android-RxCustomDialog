import com.fpliu.gradle.bintrayUploadExtension
import java.util.Properties

buildscript {
    repositories {
        jcenter()
        google()
    }
    dependencies {
        //对android-maven-gradle-plugin和gradle-bintray-plugin两个插件的包装、简化插件
        //https://github.com/leleliu008/BintrayUploadGradlePlugin
        classpath("com.fpliu:BintrayUploadGradlePlugin:1.0.0")
    }
}

apply {
    plugin("com.fpliu.bintray")
}

plugins {
    id("com.android.library")

    id("kotlin-android")

    //用于构建jar和maven包
    //https://github.com/dcendents/android-maven-gradle-plugin
    id("com.github.dcendents.android-maven").version("2.0")

    //用于上传maven包到jCenter中
    //https://github.com/bintray/gradle-bintray-plugin
    id("com.jfrog.bintray").version("1.7.3")
}

android {
    compileSdkVersion(28)

    defaultConfig {
        minSdkVersion(18)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "2.0.0"
    }

    sourceSets {
        getByName("main") {
            jniLibs.srcDir("src/main/libs")
            aidl.srcDirs("src/main/kotlin")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    lintOptions {
        isAbortOnError = false
    }
    compileOptions {
        //使用JAVA8语法解析
        setSourceCompatibility(JavaVersion.VERSION_1_8)
        setTargetCompatibility(JavaVersion.VERSION_1_8)
    }
}

dependencies {
    //http://kotlinlang.org/docs/reference/using-gradle.html#configuring-dependencies
    api(kotlin("stdlib", rootProject.extra["kotlinVersion"] as String))

    //https://github.com/JakeWharton/RxBinding
    api("com.jakewharton.rxbinding3:rxbinding:3.0.0-alpha2")

    //https://bintray.com/fpliu/newton
    api("com.fpliu:Android-CustomDialog:1.0.0")

    //https://github.com/uber/AutoDispose
    api("com.uber.autodispose:autodispose-ktx:1.1.0")
}

// 这里是groupId，必须填写,一般填你唯一的包名
group = "com.fpliu"

//这个是版本号，必须填写
version = android.defaultConfig.versionName ?: "1.0.0"

val rootProjectName: String = project.rootProject.name
val properties = Properties().apply { load(rootProject.file("local.properties").inputStream()) }

bintrayUploadExtension {
    developerName = "leleliu008"
    developerEmail = "leleliu008@gamil.com"

    projectSiteUrl = "https://github.com/$developerName/$rootProjectName"
    projectGitUrl = "https://github.com/$developerName/$rootProjectName"

    bintrayUserName = "fpliu"
    bintrayOrganizationName = "fpliu"
    bintrayRepositoryName = "newton"
    bintrayApiKey = properties.getProperty("bintray.apikey")
}