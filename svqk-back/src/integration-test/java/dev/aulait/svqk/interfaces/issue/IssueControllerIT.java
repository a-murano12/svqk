package dev.aulait.svqk.interfaces.issue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.aulait.svqk.arch.test.ConstraintViolationResponseDto;
import dev.aulait.svqk.arch.test.ValidationMessageUtils;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

@QuarkusIntegrationTest
class IssueControllerIT {

  IssueClient client = new IssueClient();

  @Test
  void testCrud() {
    IssueDto issue = new IssueDto();
    issue.setSubject("test subject: " + RandomStringUtils.randomAlphanumeric(5));

    IssueStatusDto status = new IssueStatusDto();
    status.setId("1");
    issue.setIssueStatus(status);

    int issueId = client.save(issue).getId();

    IssueDto createdIssue = client.get(issueId);

    createdIssue.setSubject("test subject: " + RandomStringUtils.randomAlphanumeric(5));
    client.save(createdIssue);

    IssueDto updatedIssue = client.get(issueId);

    assertEquals(createdIssue.getSubject(), updatedIssue.getSubject());
  }

  @Test
  void testCreateButValidationError() {
    IssueDto issue = new IssueDto();

    ConstraintViolationResponseDto error = client.createButValidationError(issue);

    assertEquals(
        ValidationMessageUtils.getNotBlankMsg(), error.getViolations().get(0).getMessage());
  }
}
