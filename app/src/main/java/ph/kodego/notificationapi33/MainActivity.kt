package ph.kodego.notificationapi33

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat

class MainActivity : AppCompatActivity() {

    private val notificationRequestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val showNotificationButton: Button = findViewById(R.id.show_notification_button)
        val requestNotificationButton: Button = findViewById(R.id.request_notification_button)

        requestNotificationButton.setOnClickListener {
            requestNotificationPermission()
        }

        var notificationId = 1

        showNotificationButton.setOnClickListener {
            showNotification(notificationId)
            notificationId++
        }
    }

    private fun showNotification(id: Int) {

        // Create an Intent for the activity you want to start
        val resultIntent = Intent(this, MainActivity::class.java)
        // Create the TaskStackBuilder
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(resultIntent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(applicationContext, "channel_id")
            .setContentText("This is a content text")
            .setContentTitle("Title!")
            .setContentIntent(resultPendingIntent)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
        notificationManager.notify(id, notification)
    }

    private fun requestNotificationPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.POST_NOTIFICATIONS)){
            deniedPermissionDialog("Notification Permission Required", " App need notification permission to enable this feature.")
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationRequestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun deniedPermissionDialog(title: String, message: String){

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ok"){dialog, _-> dialog.dismiss()}

        builder.create().show()

    }
}