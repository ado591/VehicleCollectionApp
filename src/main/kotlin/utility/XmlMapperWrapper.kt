package utility

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule


class XmlMapperWrapper{
    companion object {
        val xmlObjectMapper: XmlMapper = XmlMapper.builder().addModule(JavaTimeModule()).build()
    }
}