# Mi Proyecto
# firebase_login_kotlin
# Micoleg Android

## Descripción


## Instalación

Para instalar el proyecto en tu máquina local, sigue estos pasos:

1. Clona el repositorio.
2. Abre el proyecto en Android Studio.
3. Sigue las instrucciones para instalar las dependencias necesarias.
4. Configura las API_KEY de Google y Facebook
5. Configura el archivo de firebase google-services.json

## Configuración del SDK de Google
Para utilizar las características de Google en la aplicación, necesitas configurar el WEB TOKEN con las siguientes cadenas en tu archivo `strings.xml`:
```xml
<resources>
     <string name="default_web_client_id">YOUR_GOOGLE_OATH2_CLIENT_TOKEN</string>
</resources>
```
## Configuración del SDK de Facebook
Para utilizar las características de Facebook en la aplicación, necesitas configurar el `AndroidManifest.xml`:
```xml
<application>
...
        <meta-data
            android:name="com.facebook.sdk.ApplicationId"
            android:value="@string/facebook_app_id" />
        <meta-data
            android:name="com.facebook.sdk.ClientToken"
            android:value="@string/facebook_client_token" />

        <activity
            android:name="com.facebook.FacebookActivity"
            android:configChanges="keyboard|keyboardHidden|screenLayout|screenSize|orientation"
            android:label="@string/app_name" />
        <activity
            android:name="com.facebook.CustomTabActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />

                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />

                <data android:scheme="@string/fb_login_protocol_scheme" />
            </intent-filter>
        </activity>
    </application>
```

Para utilizar las características de Facebook en la aplicación, necesitas configurar el SDK con las siguientes cadenas en tu archivo `strings.xml`:

- `YOUR_FACEBOOK_APP_ID`
- `YOUR_FACEBOOK_PROTOCOL_SCHEME`
- `YOUR_FACEBOOK_CLIENT_TOKEN`

Estos son placeholders que debes reemplazar con los valores reales en tu archivo `strings.xml`. Aquí tienes un ejemplo de cómo hacerlo:

```xml
<resources>	
	<string name="facebook_app_id">XXXXXXXXXXXXXXXX</string>
    <string name="fb_login_protocol_scheme">fbXXXXXXXXXXXXXXXX</string>
	<string name="facebook_client_token">YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY</string>
</resources>
```
## Configuración del build.gradle.kts (:app)
```kotlin
dependencies {
    // Login Google Implementation
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth-ktx")
    //implementation("com.google.firebase:firebase-auth:23.0.0")
    // Also add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    //
    //implementation("com.facebook.android:facebook-android-sdk:[4,5)")
    implementation("com.facebook.android:facebook-android-sdk:latest.release")
    implementation("com.google.firebase:firebase-auth")

}
```
## Configuración del build.gradle.kts (app_name)
```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false

}
```
## DOCUMENTACIÓN

### **Google**
- **Documentación de Google Sign-In**: [Credential Manager](https://developer.android.com/identity/sign-in/credential-manager-siwg)
- **Guía de Firebase para Autenticación con Google**: [Firebase Google Sign-In](https://firebase.google.com/docs/auth/android/google-signin?hl=es-419#kotlin+ktx)
- **Configuración de Firebase en Android**: [Configuración de Firebase](https://firebase.google.com/docs/android/setup?hl=es-419#kotlin)
- **Configuración del Proyecto en Firebase Console**: [Firebase Console](https://console.firebase.google.com/project/fir-login-7a6a6/settings/general/android:com.rodnal.dev.login_google?hl=es-419)
- **Tutorial de Google Sign-In con Firebase**: [Visual Android Blog](https://visualandroidblog.blogspot.com/2023/04/google-sign-in-firebase-android-kotlin-tutorial.html)

### **Facebook**
- **Guía de Inicio Rápido de Facebook Login**: [Facebook Quickstart](https://developers.facebook.com/apps/1085169469923065/use_cases/customize/quickstart/?use_case_enum=FB_LOGIN&product_route=fb-login)
- **Documentación de Firebase para Autenticación con Facebook**: [Firebase Facebook Login](https://firebase.google.com/docs/auth/android/facebook-login?hl=es-419)
- **Configuración de Proveedores de Autenticación en Firebase Console**: [Firebase Authentication Providers](https://console.firebase.google.com/project/fir-login-7a6a6/authentication/providers)
- **Video Tutorial de Facebook Login**: [YouTube Tutorial](https://www.youtube.com/watch?v=6oMf_lXg7sI&list=PLSrm9z4zp4mEwkIcaZzq__H8WuR2W_t5v&index=5)
- **Integración de Facebook en Android**: [Data Flair Blog](https://data-flair.training/blogs/facebook-integration-in-android/)

### **Comandos**
- **Generar firma de la app para modo release**:
  ```bash
  keytool -list -v -keystore "D:\KotlinApps\keys\key-login-firebase.jks" -alias key0
  ```
- **Generar key para Facebook**:
   ```bash
   keytool -exportcert -alias androiddebugkey -keystore "C:\Users\Rond_\.android\debug.keystore" | "C:\WindowsApps\openssl\bin\openssl" sha1 -binary | "C:\WindowsApps\openssl\bin\openssl" base64
  ```