package kr.wiselight.metaverse.backend.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@NoArgsConstructor
@XmlRootElement(name = "ServerResponse")
public class OpenSimSetPasswordResponseDto {

    @XmlElement(name = "Result")
    private String result;
}
