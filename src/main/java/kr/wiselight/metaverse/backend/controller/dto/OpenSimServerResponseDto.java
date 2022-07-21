package kr.wiselight.metaverse.backend.controller.dto;

import lombok.Getter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@XmlRootElement(name = "ServerResponse")
public class OpenSimServerResponseDto {

    @XmlElement(name = "result")
    private Result result;

    @Getter
    @XmlRootElement(name = "result")
    public static class Result  {
        @XmlAttribute(name = "type")
        private String type;
    }
}
