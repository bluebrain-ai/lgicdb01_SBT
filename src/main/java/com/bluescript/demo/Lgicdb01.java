package com.bluescript.demo;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import io.swagger.annotations.ApiResponses;
import com.bluescript.demo.dto.IGetCustomerJpaDto;
import com.bluescript.demo.jpa.IGetCustomerJpa;
import com.bluescript.demo.model.WsHeader;
import com.bluescript.demo.model.ErrorMsg;
import com.bluescript.demo.model.EmVariable;
import com.bluescript.demo.model.Db2Customer;
import com.bluescript.demo.model.Dfhcommarea;
import com.bluescript.demo.model.CaCustomerRequest;
import com.bluescript.demo.model.CaCustsecrRequest;
import com.bluescript.demo.model.CaPolicyRequest;
import com.bluescript.demo.model.CaPolicyCommon;
import com.bluescript.demo.model.CaEndowment;
import com.bluescript.demo.model.CaHouse;
import com.bluescript.demo.model.CaMotor;
import com.bluescript.demo.model.CaCommercial;
import com.bluescript.demo.model.CaClaim;

@Getter
@Setter
@RequiredArgsConstructor
@Log4j2
@Component

@RestController
@RequestMapping("/")
@ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 400, message = "This is a bad request, please follow the API documentation for the proper request format"),
        @io.swagger.annotations.ApiResponse(code = 401, message = "Due to security constraints, your access request cannot be authorized"),
        @io.swagger.annotations.ApiResponse(code = 500, message = "The server/Application is down. Please contact support team.") })

public class Lgicdb01 {

    @Autowired
    private WsHeader wsHeader;
    @Autowired
    private ErrorMsg errorMsg;
    @Autowired
    private EmVariable emVariable;
    @Autowired
    private Db2Customer db2Customer;
    @Autowired
    private Dfhcommarea dfhcommarea;
    @Autowired
    private CaCustomerRequest caCustomerRequest;
    @Autowired
    private CaCustsecrRequest caCustsecrRequest;
    @Autowired
    private CaPolicyRequest caPolicyRequest;
    @Autowired
    private CaPolicyCommon caPolicyCommon;
    @Autowired
    private CaEndowment caEndowment;
    @Autowired
    private CaHouse caHouse;
    @Autowired
    private CaMotor caMotor;
    @Autowired
    private CaCommercial caCommercial;
    @Autowired
    private CaClaim caClaim;
    private String wsTime;
    private String wsDate;
    private String caData;
    private int wsCaHeadertrailerLen = 0;
    private int wsRequiredCaLen = +0;
    private int db2CustomernumberInt;
    private int wsCustomerLen = 0;

    private String wsAbstime;

    private String caErrorMsg;
    private int eibcalen = 0;

    private IGetCustomerJpaDto getCustomerJpaDto;

    @Autowired
    private IGetCustomerJpa getCustomerJpa;

    @Value("${api.LGSTSQ.host}")
    private String LGSTSQ_HOST;
    @Value("${api.LGSTSQ.uri}")
    private String LGSTSQ_URI;

    @PostMapping("/lgicdb01")
    public ResponseEntity<Dfhcommarea> mainline(@RequestBody Dfhcommarea payload) {
        log.debug("Methodmainlinestarted..");
        // if (eibcalen == 0) { //Not Required .. Manually Removed , Pattern based
        // automation
        // errorMsg.setEmVariable(" NO COMMAREA RECEIVED");
        // writeErrorMessage();
        // log.error("Error code :", LGCA);
        // throw new LGCAException("LGCA");

        // }
        BeanUtils.copyProperties(payload, dfhcommarea);
        System.out.println("payload:" + payload);
        log.info("dfhcommarea.getCaCustomerNum() value passed:", dfhcommarea.getCaCustomerNum());
        dfhcommarea.setCaReturnCode(00);
        wsRequiredCaLen = wsCustomerLen;
        wsRequiredCaLen = wsCaHeadertrailerLen + wsRequiredCaLen;
        if (eibcalen < wsRequiredCaLen) {
            dfhcommarea.setCaReturnCode(98); /* return */

        }
        // dfhcommarea.setCaCustomerNum(dfhcommarea.getCaCustomerNum().substring(0, 9));

        db2CustomernumberInt = (int) dfhcommarea.getCaCustomerNum();
        emVariable.setEmCusnum(String.valueOf(dfhcommarea.getCaCustomerNum()));
        getCustomerInfo();

        log.info("Method mainline completed..");
        return new ResponseEntity<>(dfhcommarea, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public void getCustomerInfo() {
        log.info("MethodgetCustomerInfostarted..");

        try {

            getCustomerJpaDto = getCustomerJpa.getCustomerByDb2CustomernumberInt(db2CustomernumberInt);
            dfhcommarea.setCaReturnCode(00);
            log.info("getCustomerJpaDto:" + getCustomerJpaDto.getCaFirstName());
            BeanUtils.copyProperties(getCustomerJpaDto, caCustomerRequest);
            log.info("getCustomerJpaDto:copied:" + caCustomerRequest.getCaFirstName());
            dfhcommarea.setCaCustomerRequest(caCustomerRequest);

        } catch (Exception e) {
            log.error(e);
            dfhcommarea.setCaReturnCode(01);
            writeErrorMessage();
        }
        log.info("Method getCustomerInfo completed..");
    }

    public void writeErrorMessage() {
        log.info("MethodwriteErrorMessagestarted..");
        wsAbstime = LocalTime.now().toString();
        wsAbstime = LocalTime.now().toString();
        wsDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMDDYYYY"));
        wsTime = LocalTime.now().toString();
        errorMsg.setEmDate(wsDate.substring(0, 8));
        errorMsg.setEmTime(wsTime.substring(0, 6));
        WebClient webclientBuilder = WebClient.create(LGSTSQ_HOST);
        try {
            Mono<ErrorMsg> lgstsqResp = webclientBuilder.post().uri(LGSTSQ_URI)
                    .body(Mono.just(errorMsg), ErrorMsg.class).retrieve().bodyToMono(ErrorMsg.class);// .timeout(Duration.ofMillis(10_000));
            errorMsg = lgstsqResp.block();
        } catch (Exception e) {
            log.error(e);
        }
        if (eibcalen > 0) {
            if (eibcalen < 91) {
                caData = (dfhcommarea.getCaRequestId().substring(0, 9));
                try {
                    Mono<String> lgstsqResp = webclientBuilder.post().uri(LGSTSQ_URI)
                            .body(Mono.just(caErrorMsg), String.class).retrieve().bodyToMono(String.class);// .timeout(Duration.ofMillis(10_000));
                    caErrorMsg = lgstsqResp.block();
                } catch (Exception e) {
                    log.error(e);
                }

            } else {
                caData = (dfhcommarea.getCaRequestId() + dfhcommarea.getCaReturnCode() + dfhcommarea.getCaCustomerNum()
                        + dfhcommarea.getCaRequestSpecific().substring(0, 72));
                try {
                    Mono<String> lgstsqResp = webclientBuilder.post().uri(LGSTSQ_URI)
                            .body(Mono.just(caErrorMsg), String.class).retrieve().bodyToMono(String.class);// .timeout(Duration.ofMillis(10_000));
                    caErrorMsg = lgstsqResp.block();
                } catch (Exception e) {
                    log.error(e);
                }

            }

        }

        log.debug("Method writeErrorMessage completed..");
    }

    /* End of program */
}