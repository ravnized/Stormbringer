package pdm.uninsubria.stormbringer.tools

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback

class Storage(private val context: Context) {
    companion object {
        private var isCloudinaryInit = false

        fun initCloudinary(context: Context) {
            if (!isCloudinaryInit) {
                val config = HashMap<String, String>()
                config["cloud_name"] = "dorqp85xq"
                config["secure"] = "true"
                MediaManager.init(context, config)
                isCloudinaryInit = true
            }
        }
    }

    init {
        try {
            // Usa applicationContext per sicurezza
            initCloudinary(context.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    suspend fun uploadToCloudinary(imageUri: Uri): String? {

        return suspendCancellableCoroutine { continuation ->

            Log.d("Cloudinary", "Inizio upload per: $imageUri")

            MediaManager.get().upload(imageUri)
                .unsigned("stormbringer_preset")
                .callback(object : UploadCallback {

                    override fun onStart(requestId: String?) {
                        Log.d("Cloudinary", "Upload iniziato...")
                    }

                    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                        val progress = (bytes.toFloat() / totalBytes.toFloat() * 100).toInt()
                        Log.d("Cloudinary", "Progresso: $progress%")
                    }

                    override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {

                        val url = resultData?.get("secure_url") as? String
                        Log.d("Cloudinary", "Successo! URL: $url")


                        if (continuation.isActive) {
                            continuation.resume(url)
                        }
                    }

                    override fun onError(requestId: String?, error: ErrorInfo?) {
                        Log.e("Cloudinary", "Errore: ${error?.description}")


                        if (continuation.isActive) {
                            continuation.resume(null)
                        }
                    }

                    override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                        Log.e("Cloudinary", "Riprogrammazione: ${error?.description}")
                    }
                })
                .dispatch()
        }
    }


}