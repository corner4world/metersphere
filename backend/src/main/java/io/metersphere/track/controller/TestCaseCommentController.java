package io.metersphere.track.controller;

import io.metersphere.commons.constants.OperLogConstants;
import io.metersphere.commons.constants.RoleConstants;
import io.metersphere.log.annotation.MsAuditLog;
import io.metersphere.track.dto.TestCaseCommentDTO;
import io.metersphere.track.request.testreview.SaveCommentRequest;
import io.metersphere.track.service.TestCaseCommentService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@RequestMapping("/test/case/comment")
@RestController
public class TestCaseCommentController {

    @Resource
    private TestCaseCommentService testCaseCommentService;

    @PostMapping("/save")
    @MsAuditLog(module = "track_test_case_review", type = OperLogConstants.CREATE, content = "#msClass.getLogDetails(#request.id)", msClass = TestCaseCommentService.class)
    public void saveComment(@RequestBody SaveCommentRequest request) {
        request.setId(UUID.randomUUID().toString());
        testCaseCommentService.saveComment(request);
    }

    @GetMapping("/list/{caseId}")
    public List<TestCaseCommentDTO> getCaseComments(@PathVariable String caseId) {
        return testCaseCommentService.getCaseComments(caseId);
    }

    @GetMapping("/delete/{commentId}")
    @RequiresRoles(value = {RoleConstants.TEST_USER, RoleConstants.TEST_MANAGER}, logical = Logical.OR)
    @MsAuditLog(module = "track_test_case_review", type = OperLogConstants.DELETE, beforeEvent = "#msClass.getLogDetails(#commentId)", msClass = TestCaseCommentService.class)
    public void deleteComment(@PathVariable String commentId) {
        testCaseCommentService.delete(commentId);
    }

    @PostMapping("/edit")
    @MsAuditLog(module = "track_test_case_review", type = OperLogConstants.UPDATE, beforeEvent = "#msClass.getLogDetails(#request.id)", content = "#msClass.getLogDetails(#request.id)", msClass = TestCaseCommentService.class)
    public void editComment(@RequestBody SaveCommentRequest request) {
        testCaseCommentService.edit(request);
    }
}
