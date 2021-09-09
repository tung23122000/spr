package dts.com.vn.controller;

import dts.com.vn.entities.ConstantDeclaration;
import dts.com.vn.entities.LogAction;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.security.jwt.TokenProvider;
import dts.com.vn.service.ConstantDeclarationService;
import dts.com.vn.service.LogActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/constant-declaration")
public class ConstantDeclarationController {
    private final ConstantDeclarationService constantDeclarationService;
    private final TokenProvider tokenProvider;
    private final LogActionService logActionService;

    public ConstantDeclarationController(ConstantDeclarationService constantDeclarationService, TokenProvider tokenProvider, LogActionService logActionService) {
        this.constantDeclarationService = constantDeclarationService;
        this.tokenProvider = tokenProvider;
        this.logActionService = logActionService;
    }

    private static final Logger logger = LoggerFactory.getLogger(ConstantDeclarationController.class);

    @Transactional
    @PostMapping("/save")
    public ResponseEntity<?> saveConstantDeclaration(@RequestBody ConstantDeclaration constantDeclaration) {
        ApiResponse response;
        try {
            // Tạo Log Action
            LogAction logAction = new LogAction();
            logAction.setTableAction("constant");
            logAction.setAccount(TokenProvider.account);
            logAction.setOldValue(null);
            // Kiểm tra key đã tồn tại chưa
            ConstantDeclaration constantDeclarationExist = constantDeclarationService.findByConstantKey(constantDeclaration.getConstantKey());
            if (constantDeclarationExist == null){
                logAction.setAction("CREATE");
            }else{
                logAction.setAction("UPDATE");
            }
            ConstantDeclaration constantDeclarationReturn = constantDeclarationService.save(constantDeclaration);
            // save xong cập nhật dữ liệu vào Log Action
            logAction.setIdAction(null);
            logAction.setNewValue(constantDeclarationReturn.toString());
            logAction.setTimeAction(new Date());
            logActionService.add(logAction);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), constantDeclarationReturn);
        } catch (Exception e) {
            response = new ApiResponse(e, ErrorCode.SAVE_CONSTANT_FAILED);
            logger.error("SAVE_CONSTANT_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/find-by-constant-key")
    public ResponseEntity<?> findByConstantKey(@RequestParam String constantKey) {
        ApiResponse response;
        try {
            ConstantDeclaration constantDeclaration = constantDeclarationService.findByConstantKey(constantKey);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), constantDeclaration);
        } catch (Exception e) {
            response = new ApiResponse(e, ErrorCode.FIND_CONSTANT_FAILED);
            logger.error("FIND_CONSTANT_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }
}
