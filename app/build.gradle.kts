plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.draker.recmaster"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.draker.recmaster"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        // Используем предоставленные API ключи
        buildConfigField("String", "TMDB_API_KEY", "\"df6cc4ca6799e08fed0d53607376eda7\"")
        buildConfigField("String", "TMDB_ACCESS_TOKEN", "\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkZjZjYzRjYTY3OTllMDhmZWQwZDUzNjA3Mzc2ZWRhNyIsIm5iZiI6MTc0NzA2NzAzMy43MzksInN1YiI6IjY4MjIyMDk5MGUwMGNmMjZmNDZlZTU2OCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.4yYForZ_9GRLHerSnmEBfIOz-JlB6C9KgOMnxFHgiyA\"")
        buildConfigField("String", "TMDB_BASE_URL", "\"https://api.themoviedb.org/3/\"")
        // Google Books API не требует API ключа для большинства запросов
        buildConfigField("String", "GOOGLE_BOOKS_BASE_URL", "\"https://www.googleapis.com/books/v1/\"")
        // RAWG Games API 
        buildConfigField("String", "RAWG_API_KEY", "\"d81d4e0fba794cbe8de9d4dcefd14c5b\"")
        buildConfigField("String", "RAWG_BASE_URL", "\"https://api.rawg.io/api/\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    
    // Navigation Component
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    
    // RecyclerView и CardView
    implementation(libs.recyclerview)
    implementation(libs.cardview)
    
    // ViewModel и LiveData
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    
    // Glide для загрузки изображений
    implementation(libs.glide)
    
    // Retrofit и зависимости для работы с сетью
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.gson)
    
    // Room для локальной базы данных
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1") 
    implementation("androidx.room:room-ktx:2.6.1") // Опционально для корутин
    
    // Корутины для асинхронных операций
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    
    // Тесты
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}