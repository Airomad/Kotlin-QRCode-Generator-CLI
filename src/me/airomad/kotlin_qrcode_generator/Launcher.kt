package me.airomad.kotlin_qrcode_generator

import org.apache.commons.cli.*



/**
 * Created by Airomad on 07.05.2017.
 *
 */

fun main(args: Array<String>) {
    val options = Options()

    val string = Option("s", "string", true, "string to encode")
    string.args = 1
    string.isRequired = true
    options.addOption(string)

    val output = Option("o", "output", true, "output file name")
    output.args = 1
    output.isRequired = false
    options.addOption(output)

    val parser = DefaultParser()
    val formatter = HelpFormatter()
    val cmd: CommandLine

    try {
        cmd = parser.parse(options, args)
    } catch (e: ParseException) {
        System.out.println(e.message)
        formatter.printHelp("qrcodegen", options)

        System.exit(1)
        return
    }

    val outputValue = cmd.getOptionValue("output")
    if (outputValue != null)
        QRCodeGenerator.outputPath = outputValue

    QRCodeGenerator.data = cmd.getOptionValue("string")
    QRCodeGenerator.size = 1024
    QRCodeGenerator.generate()
    QRCodeGenerator.save()
}
