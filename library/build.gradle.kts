import com.fpliu.gradle.bintrayUploadExtension

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
    compileSdkVersion(27)
    buildToolsVersion("27.0.3")

    defaultConfig {
        minSdkVersion(11)
        targetSdkVersion(25)
        versionCode = 1
        versionName = "1.0.0"
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

// 这里是groupId，必须填写,一般填你唯一的包名
group = "com.fpliu"

//这个是版本号，必须填写
version = "1.0.0"

val rootProjectName: String = project.rootProject.name

bintrayUploadExtension {
    developerName = "leleliu008"
    developerEmail = "leleliu008@gamil.com"

    projectSiteUrl = "https://github.com/$developerName/$rootProjectName"
    projectGitUrl = "https://github.com/$developerName/$rootProjectName"

    bintrayUserName = "fpliu"
    bintrayOrganizationName = "fpliu"
    bintrayRepositoryName = "newton"
    bintrayApiKey = "xxxxxxxxxxxxxxxxxxxxxxxxxxxx"
}

dependencies {
    api("com.fpliu:Android-CustomDialog:1.0.0")

    //http://kotlinlang.org/docs/reference/using-gradle.html#configuring-dependencies
    api("org.jetbrains.kotlin:kotlin-stdlib:1.2.21")

    api("com.android.support:support-annotations:27.1.1")

    //https://github.com/ReactiveX/RxJava
    api("io.reactivex.rxjava2:rxandroid:2.0.2")

    //https://github.com/JakeWharton/RxBinding
    api("com.jakewharton.rxbinding2:rxbinding:2.0.0")

    //https://github.com/trello/RxLifecycle
    api("com.trello.rxlifecycle2:rxlifecycle:2.2.1")
}
