import java.io.FilterInputStream
import java.io.IOException
import java.io.InputStream

class ProgressInputStream(
    inputStream: InputStream,
    private val totalBytes: Long,
) : FilterInputStream(inputStream) {
    private var bytesRead: Long = 0

    @Throws(IOException::class)
    override fun read(): Int {
        val byte = super.read()
        if (byte != -1) {
            bytesRead++
            showProgress()
        }
        return byte
    }

    @Throws(IOException::class)
    override fun read(
        b: ByteArray,
        off: Int,
        len: Int,
    ): Int {
        val n = super.read(b, off, len)
        if (n != -1) {
            bytesRead += n
            showProgress()
        }
        return n
    }

    private fun showProgress() {
        val progress = (bytesRead.toDouble() / totalBytes) * 100
        println("Progress: %.2f%%".format(progress))
    }
}
