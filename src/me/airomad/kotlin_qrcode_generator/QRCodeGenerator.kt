package me.airomad.kotlin_qrcode_generator

import java.io.IOException
import com.google.zxing.WriterException
import javax.imageio.ImageIO
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.google.zxing.EncodeHintType
import java.awt.Color
import java.util.EnumMap
import java.io.File



/**
 * Created by Airomad on 07.05.2017.
 *
 */

object QRCodeGenerator {
    var data: String? = null
    var size: Int = 32
        set(value) {
            field = if (value > 32) value else 32
        }
    var outputType: String = "png"
        set(value) {
            value.apply {
                field = toLowerCase()
                if (field != "png" || field != "jpg")
                    field = "png"
            }
        }
    var outputPath: String = System.getProperty("user.home") + "\\output.$outputType"
        set(value) {
            field = value
            if (value.endsWith("/") || value.endsWith("\\"))
                field += "output.$outputType"
        }
    private var outputImage: BufferedImage? = null

    fun generate() {
        if (data == null)
            throw IllegalStateException("Data to encode is empty!")

        try {
            val hintMap = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8")

            hintMap.put(EncodeHintType.MARGIN, 1)
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L)

            val qrCodeWriter = QRCodeWriter()
            val byteMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, size,
                    size, hintMap)
            val width = byteMatrix.width
            outputImage = BufferedImage(width, width, BufferedImage.TYPE_INT_RGB)
            outputImage?.createGraphics()

            val graphics = outputImage?.graphics as Graphics2D
            graphics.color = Color.WHITE
            graphics.fillRect(0, 0, width, width)
            graphics.color = Color.BLACK

            for (i in 0..width - 1) {
                for (j in 0..width - 1) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1)
                    }
                }
            }
        } catch (e: WriterException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        println("QRCodeGenerator: QR Code was generated.")
    }

    fun save() {
        val outputFile = File(outputPath)
        ImageIO.write(outputImage, outputType, outputFile)
        println("QRCodeGenerator: Output image saved at ${outputFile.absolutePath}.")
    }
}
