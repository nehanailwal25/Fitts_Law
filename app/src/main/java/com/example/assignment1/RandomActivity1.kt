package com.example.assignment1

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.view.View
import android.widget.AbsoluteLayout
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.assignment1.models.ButtonDimension
import com.example.assignment1.models.ButtonPossition
import com.example.assignment1.models.TrialDto
import java.io.File
import java.lang.StringBuilder
import kotlin.math.log2


class RandomActivity1 : AppCompatActivity() , View.OnClickListener {

    private val FILE_NAME = "trial.txt"

    private var buttonRandom: Button? = null
    private var shareButton: Button? = null
    private var startButton: Button? = null
    private var restartExperimentButton: Button? = null
    private var absolute_id: AbsoluteLayout? = null

    // trail number
    private var currentTrial = 1
    // event counter
    private var partCount = 1

    private var device = "Finger"

    // possition of last drawn button
    private var lastCeterX = -1
    private var lastCenterY = -1
    private var lastWidth = 0
    private var lastHeight = 0

    // available screen size
    private var screenWidth = 0
    private var screenHeight = 0
    // button dimensions
    private var buttonWidth = 0
    private var buttonHeight = 0
    private var distance = 0

    private var shuffler = intArrayOf(1, 2, 3, 4) // x, y, -x, -y

    private var errorCount = 0

    private var starTimestemp = System.currentTimeMillis()

    /// Timer
    var START_MILLI_SECONDS = 600000L // start countdown from 6 lack millisecond = 600 seconds = 10 minutes
    lateinit var countdown_timer: CountDownTimer
    var isRunning: Boolean = false;
    var time_in_milli_seconds = 0L

    // positions for first button in each trial
    var initialPossitions = arrayListOf<ButtonPossition>()
    var buttonDimensions = arrayListOf<ButtonDimension>()
    // details got from ech trial
    val trialsData = mutableListOf<TrialDto>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random1)

        // get button's refarence
        buttonRandom = findViewById(R.id.my_button)
        shareButton = findViewById(R.id.share_button)
        startButton = findViewById(R.id.start_button)
        restartExperimentButton = findViewById(R.id.restart_experiment_button)
        absolute_id = findViewById(R.id.absolute_id)

        setup()
        // button actions
        setlistener()
        // show buttons randomaly
        shareButton?.setVisibility(View.INVISIBLE)
        startButton?.setVisibility(View.VISIBLE)
        buttonRandom?.setVisibility(View.INVISIBLE)
        restartExperimentButton?.setVisibility(View.INVISIBLE)
    }

    fun setup() {
        val displaymetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displaymetrics)
        // screen's drawable width and height
        screenWidth = displaymetrics.widthPixels
        screenHeight = displaymetrics.heightPixels - 150


        initialPossitions.add(ButtonPossition(xValue = 70, yValue = 70))
        initialPossitions.add(ButtonPossition(xValue = screenWidth-70, yValue = 70))
        initialPossitions.add(ButtonPossition(xValue = 90, yValue = 90))
        initialPossitions.add(ButtonPossition(xValue = 110, yValue = screenHeight-110))
        initialPossitions.add(ButtonPossition(xValue = screenWidth-70, yValue = screenHeight-70))
        initialPossitions.add(ButtonPossition(xValue = 90, yValue = 90))
        initialPossitions.add(ButtonPossition(xValue = screenWidth-110, yValue = 110))
        initialPossitions.add(ButtonPossition(xValue = 70, yValue = screenHeight-70))
        initialPossitions.add(ButtonPossition(xValue = screenWidth-90, yValue = 90))
        initialPossitions.add(ButtonPossition(xValue = 110, yValue = 110))


        buttonDimensions.add(ButtonDimension(width = 120, height = 120, distance = 80))
        buttonDimensions.add(ButtonDimension(width = 160, height = 160, distance = 80))
        buttonDimensions.add(ButtonDimension(width = 200, height = 200, distance = 80))
        buttonDimensions.add(ButtonDimension(width = 120, height = 120, distance = 200))
        buttonDimensions.add(ButtonDimension(width = 160, height = 160, distance = 200))
        buttonDimensions.add(ButtonDimension(width = 200, height = 200, distance = 200))
        buttonDimensions.add(ButtonDimension(width = 120, height = 120, distance = 420))
        buttonDimensions.add(ButtonDimension(width = 160, height = 160, distance = 420))
        buttonDimensions.add(ButtonDimension(width = 200, height = 200, distance = 420))
    }

    /// Button action
    fun setlistener() {
        // button action
        buttonRandom!!.setOnClickListener(this)

        // Share action
        shareButton?.setOnClickListener {
            // create file and share
            createCSV()
        }

        // start action
        startButton?.setOnClickListener {

            startButton?.setVisibility(View.INVISIBLE)
            buttonRandom?.setVisibility(View.VISIBLE)

            // show next button
            didButtonPress()
        }

        restartExperimentButton?.setOnClickListener {

            // restart the whole experiment for thumb now
            device = "Thumb"
            partCount = 1
            currentTrial = 1

            // experiment end for current device
            buttonRandom?.setVisibility(View.VISIBLE)
            shareButton?.setVisibility(View.INVISIBLE)
            restartExperimentButton?.setVisibility(View.INVISIBLE)

            // show next button
            didButtonPress()
        }

        absolute_id?.setOnClickListener {
            errorCount ++
            starTimestemp = System.currentTimeMillis()
        }
    }

    override fun onClick(v: View?) {
        didButtonPress()
    }

    fun didButtonPress() {

        val timeConsumed = System.currentTimeMillis() - starTimestemp

        // pause timer
        if (isRunning) {
            pauseTimer()
        }

        println("---------------------------------------")
        val timeTaken = START_MILLI_SECONDS - time_in_milli_seconds
        val preDistance = distance
        println("timeTaken ->")
        println(timeTaken)
        println("timeConsumed ->")
        println(timeConsumed)
        println("preDistance")
        println(preDistance)
        println("---------------------------------------")


        // if all button of trails pressed
        if (currentTrial == 10 && partCount == 10) {

            // check current device
            if (device == "Thumb") {
                // experiment end for current device
                buttonRandom?.setVisibility(View.INVISIBLE)
                shareButton?.setVisibility(View.VISIBLE)
                restartExperimentButton?.setVisibility(View.INVISIBLE)

                return
            } else {
                // option to restart for next device
                buttonRandom?.setVisibility(View.INVISIBLE)
                restartExperimentButton?.setVisibility(View.VISIBLE)

                return
            }
        } else {
            // if last button of trail
            if (partCount == 10) {
                partCount = 1
                currentTrial++
            }
        }

//        if (partCount == 1) {
//            // option to restart for next device
//            buttonRandom?.setVisibility(View.INVISIBLE)
//            startButton?.setText("Start next")
//            startButton?.setVisibility(View.VISIBLE)
//
//            // save details
//            val trialDto = TrialDto(buttonWidth = buttonWidth,
//                    buttonHeight = buttonHeight,
//                    time = timeConsumed, // timetaken
//                    distance = preDistance,
//                    trialNumber = currentTrial,
//                    partCount = partCount,
//                    device = device,
//                    buttonx = buttonRandom!!.x,
//                    buttony = buttonRandom!!.y,
//                    indexOfDiff = indexCalc(distance, buttonWidth), errorCount = errorCount)
//
//            trialsData.add(trialDto)
//
//            // reset error count
//            errorCount = 0
//            // update counter
//            partCount++
//            // reset array
//            shuffler = intArrayOf(1, 2, 3, 4)
//
//            return
//        }

        // button dimensions
        val dimension = buttonDimensions[partCount-1]

        buttonWidth = dimension.width
        buttonHeight = dimension.height
        distance = dimension.distance

        // show button
        showRandomButtons {

            // update title
            setTitle("Device: " + device + ", Trail: "+ currentTrial +", Count: "+ partCount)

            // set button text
            buttonRandom?.setText("B" + partCount)

            // save details
            val trialDto = TrialDto(buttonWidth = buttonWidth,
                buttonHeight = buttonHeight,
                time = timeConsumed, // timetaken
                distance = preDistance,
                trialNumber = currentTrial,
                partCount = partCount,
                device = device,
                buttonx = buttonRandom!!.x,
                buttony = buttonRandom!!.y,
                indexOfDiff = indexCalc(distance, buttonWidth), errorCount = errorCount)

            trialsData.add(trialDto)

            // reset error count
            errorCount = 0
            // update counter
            partCount++
            // reset array
            shuffler = intArrayOf(1, 2, 3, 4)

            // reset timer
            resetTimer()

            println("---------------------------------------")
            println("startTimer(START_MILLI_SECONDS)")
            println(START_MILLI_SECONDS)
            println("---------------------------------------")
            // restart
            startTimer(START_MILLI_SECONDS)

            starTimestemp = System.currentTimeMillis()
        }
    }

    private fun indexCalc(distance: Int, buttonWidth: Int): Float {
        var value: Float = (distance.toFloat()/buttonWidth.toFloat())+1
        return log2(value)
    }

    // Show random button
    fun showRandomButtons(completion : () -> Unit ) {
        val absParams =
            buttonRandom?.getLayoutParams() as AbsoluteLayout.LayoutParams
        buttonRandom?.measure(
            View.MeasureSpec.UNSPECIFIED,
            View.MeasureSpec.UNSPECIFIED
        )

        // for 1st button, update stored possion of last drawn button
//        if (partCount == 1) {

//            // get random position for button
//            // val r = Random()
//            val x: Int =  initialPossitions[currentTrial - 1].xValue
//            val y: Int =  initialPossitions[currentTrial - 1].yValue
//
//            lastCeterX = x
//            lastCenterY = y
//
//            // assign position to layout
//            absParams.x = x-buttonWidth/2
//            absParams.y = y-buttonHeight/2
//            absParams.width=buttonWidth;
//            absParams.height=buttonHeight;
//            buttonRandom?.setLayoutParams(absParams)
//
//            lastWidth = buttonWidth
//
//            completion()

//        } else {

            if (shuffler.count() == 0){
                val x: Int =  initialPossitions[currentTrial - 1].xValue
                val y: Int =  initialPossitions[currentTrial - 1].yValue

                lastCeterX = x
                lastCenterY = y

                // assign position to layout
                absParams.x = x-buttonWidth/2
                absParams.y = y-buttonHeight/2
                absParams.width=buttonWidth;
                absParams.height=buttonHeight;
                buttonRandom?.setLayoutParams(absParams)

                lastWidth = buttonWidth

                completion()

                return
            }

            // get random value
            val pick = shuffler.random()

            when (pick) {
                // right
                1 -> {
                    val nextX = lastCeterX + distance - buttonWidth/2 // from center to center
                    val availableRightSpace = screenWidth - (lastCeterX + lastWidth/2) // avaialble space

                    if ((nextX+buttonWidth) <= availableRightSpace) {

                        lastCeterX = lastCeterX + distance
                        lastCenterY = lastCenterY

                        // assign position to layout
                        absParams.x = nextX
                        absParams.y = lastCenterY
                        absParams.width=buttonWidth;
                        absParams.height=buttonHeight;
                        // absParams.width=500;
                        buttonRandom?.setLayoutParams(absParams)

                        lastWidth = buttonWidth

                        completion()
                   } else {
                        shuffler = remove(shuffler, item = pick)
                        // draw button again
                        showRandomButtons(completion)
                   }
                }
                // downward
                2 -> {
                    val nextY = lastCenterY + distance - buttonHeight/2  // from center to center
                    val availableDownSpace = screenHeight - (lastCenterY + lastHeight/2) // available space

                    if ((nextY+buttonHeight) <= availableDownSpace) {

                        lastCeterX = lastCeterX
                        lastCenterY = lastCenterY + distance

                        // assign position to layout
                        absParams.x = lastCeterX
                        absParams.y = nextY
                        absParams.width=buttonWidth;
                        absParams.height=buttonHeight;
                        // absParams.width=500;
                        buttonRandom?.setLayoutParams(absParams)

                        lastWidth = buttonWidth

                        completion()
                    } else {
                        shuffler = remove(shuffler, item = pick)
                        // draw button again
                        showRandomButtons(completion)
                    }
                }
                // left
                3 -> {
                    val nextX = ((lastCeterX - distance) - buttonWidth/2)  // from center from left
                    val availableLeftSpace = (lastCeterX-buttonWidth/2) // avaialble space on left

                    if (nextX > 0 && nextX <= availableLeftSpace) {

                        lastCeterX = lastCeterX - distance
                        lastCenterY = lastCenterY

                        // assign position to layout
                        absParams.x = nextX
                        absParams.y = lastCenterY
                        absParams.width=buttonWidth;
                        absParams.height=buttonHeight;
                        // absParams.width=500;
                        buttonRandom?.setLayoutParams(absParams)

                        lastWidth = buttonWidth

                        completion()
                    } else {

                        shuffler = remove(shuffler, item = pick)

                        // draw button again
                        showRandomButtons(completion)
                    }
                }
                // upward
                4 -> {
                    val nextY = (lastCenterY - distance - buttonHeight/2)  // from center to cennter toward up
                    val availableUpSpace = lastCenterY-buttonHeight/2 // available space

                    if (nextY > 0 && nextY <= availableUpSpace) {

                        lastCeterX = lastCeterX
                        lastCenterY = lastCenterY - distance

                        // assign position to layout
                        absParams.x = lastCeterX
                        absParams.y = nextY
                        absParams.width=buttonWidth;
                        absParams.height=buttonHeight;
                        // absParams.width=500;
                        buttonRandom?.setLayoutParams(absParams)

                        lastWidth = buttonWidth

                        completion()
                    } else {
                        shuffler = remove(shuffler, item = pick)
                        // draw button again
                        showRandomButtons(completion)
                    }
                }
            }
            println("shuffler")
            println(shuffler.iterator())
//        }
    }

/// Timer
    private fun pauseTimer() {

        countdown_timer.cancel()
        isRunning = false
    }

    private fun startTimer(time_in_seconds: Long) {
        countdown_timer = object : CountDownTimer(time_in_seconds, 1) {
            override fun onFinish() {
                // loadConfeti()
            }

            override fun onTick(p0: Long) {
                time_in_milli_seconds = p0
            }
        }
        countdown_timer.start()

        isRunning = true
    }

    private fun resetTimer() {
        time_in_milli_seconds = START_MILLI_SECONDS
        // updateTextUI()
        // reset.visibility = View.INVISIBLE
    }

    fun sendEmail(){

        var message = ""
        trialsData.forEach {trialDto ->
            message = message + "device" +" : "+ trialDto.device + "\n"
            message = message + "trialNumber" +" : "+ trialDto.trialNumber + "\n"
            message = message +  "partCount" +" : "+ trialDto.partCount + "\n"
            message = message +  "buttonWidth" +" : "+ trialDto.buttonWidth + "\n"
            message = message +  "buttonHeight" +" : "+ trialDto.buttonHeight + "\n"
            message = message +  "time" +" : "+ trialDto.time + "\n"
            message = message +  "distance" +" : "+ trialDto.distance + "\n \n"
        }


        val email = Intent(Intent.ACTION_SEND)
        email.putExtra(Intent.EXTRA_SUBJECT, "Trial")
        email.putExtra(Intent.EXTRA_TEXT, message)
        //need this to prompts email client only
        email.type = "message/rfc822"

        startActivity(Intent.createChooser(email, "Choose an Email client :"))
    }


    fun createCSV() {

        val data = StringBuilder()
        data.append("Device, Trial Number,Part Count,Button width, Button Height,Movement Time, Distance,X-Location, Y-Location, Index Of Difficulty, Error Count")
        data.append("\n")

        trialsData.forEach {trialDto ->
            data.append(trialDto.device)
            data.append(',')
            data.append(trialDto.trialNumber)
            data.append(',')
            data.append(trialDto.partCount)
            data.append(',')
            data.append(trialDto.buttonWidth)
            data.append(',')
            data.append(trialDto.buttonHeight)
            data.append(',')
            data.append(trialDto.time)
            data.append(',')
            data.append(trialDto.distance)
            data.append(",")
            data.append(trialDto.buttonx)
            data.append(",")
            data.append(trialDto.buttony)
            data.append(",")
            data.append(trialDto.indexOfDiff)
            data.append(",")
            data.append(trialDto.errorCount)
            data.append('\n')
        }

        //var fileWriter: FileWriter? = null
        try{
            val out: java.io.FileOutputStream = openFileOutput("data.csv", Context.MODE_PRIVATE)
            out.write((data.toString()).toByteArray())
            out.close()

            val context = applicationContext
            val filelocation = File(filesDir, "data.csv")
            val path: Uri = FileProvider.getUriForFile(context, "com.example.exportcsv.fileprovider", filelocation)
            val fileIntent = Intent(Intent.ACTION_SEND)
            fileIntent.setType("text/csv")
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data")
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            fileIntent.putExtra(Intent.EXTRA_STREAM, path)
            startActivity(Intent.createChooser(fileIntent, "Send mail"))
        }
        catch(e: Exception){
            println("Writing CSV error!")
            e.printStackTrace()
        }
    }

    fun remove(arr: IntArray, item: Int): IntArray {
         return arr.filter { i: Int -> i != item }.toIntArray()
    }
}