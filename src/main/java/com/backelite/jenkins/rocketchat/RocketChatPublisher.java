package com.backelite.jenkins.rocketchat;

import com.backelite.jenkins.rocketchat.api.RocketChatAPIClient;
import com.backelite.jenkins.rocketchat.api.RocketChatAPIException;
import com.backelite.jenkins.rocketchat.api.RocketChatPostAttachment;
import com.backelite.jenkins.rocketchat.api.RocketChatPostPayload;
import hudson.Launcher;
import hudson.model.*;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.test.AbstractTestResultAction;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by gillesgrousset on 07/07/2016.
 */
public class RocketChatPublisher extends Notifier implements Serializable {

    private String channel;
    private boolean notifyBackToNormalOnly;
    private boolean showTestSummary;

    @DataBoundConstructor
    public RocketChatPublisher(String channel, boolean notifyBackToNormalOnly, boolean showTestSummary) {
        this.setChannel(channel);
        this.notifyBackToNormalOnly = notifyBackToNormalOnly;
        this.showTestSummary = showTestSummary;
    }

    @Override
    public RocketChatDescriptor getDescriptor() {
        return (RocketChatDescriptor) super.getDescriptor();
    }

    @Override
    public boolean needsToRunAfterFinalized() {
        return true;
    }

    @Override
    public Action getProjectAction(AbstractProject<?, ?> project) {
        return null;
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {

        listener.getLogger().println("Notifying Rocket.Chat");

        // Load and expand global settings
        String webhookURL = this.getDescriptor().getWebhookUrl();

        // API client instance
        RocketChatAPIClient client = new RocketChatAPIClient(webhookURL, listener.getLogger());

        // Build payload
        RocketChatPostPayload payload = this.buildPayload(build);


        // Post message
        try {
            client.post(payload);
            return true;
        } catch (RocketChatAPIException e) {
            // Log but do not mark build as failed
            listener.getLogger().println("Failed to notify Rocket.Chat: " + e.getMessage());
            return false;
        }



    }

    private RocketChatPostPayload buildPayload(AbstractBuild<?, ?> build) {

        RocketChatPostPayload payload = new RocketChatPostPayload();


        // Detect back to normal
        boolean backToNormal = false;
        if (build.getPreviousBuild() != null) {
            if (build.getResult() == Result.SUCCESS &&
                    (build.getPreviousBuild().getResult() == Result.FAILURE || build.getPreviousBuild().getResult() == Result.UNSTABLE)) {
                backToNormal = true;
            }
        } else {
            backToNormal = true;
        }



        // Project info
        String projectName = build.getProject().getName();
        String buildURL = build.getProject().getAbsoluteUrl() + build.getNumber();


        // Channel
        payload.setChannel(this.getChannel());

        // Title
        if (backToNormal) {
            payload.setText(String.format(":heavy_check_mark: Job [*%s - #%d*](%s) is back to normal", projectName, build.getNumber(), buildURL));
        } else if (build.getResult() == Result.FAILURE) {
            payload.setText(String.format(":x: Job [*%s - #%d*](%s) failed", projectName, build.getNumber(), buildURL));
        } else if (build.getResult() == Result.UNSTABLE) {
            payload.setText(String.format(":x: Job [*%s - #%d*](%s) is unstable", projectName, build.getNumber(), buildURL));
        } else if (!notifyBackToNormalOnly) {
            payload.setText(String.format(":heavy_check_mark: Job [*%s - #%d*](%s) is successful", projectName, build.getNumber(), buildURL));
        }

        // Test results (as attachment)
        AbstractTestResultAction<?> action = build.getAction(AbstractTestResultAction.class);
        if (action != null) {

            int total = action.getTotalCount();
            int failed = action.getFailCount();
            int skipped = action.getSkipCount();
            int passed = total - failed - skipped;

            RocketChatPostAttachment testAttachment = new RocketChatPostAttachment();
            payload.getAttachments().add(testAttachment);
            // Title
            testAttachment.setTitle("Tests");
            testAttachment.setTitleLink(build.getProject().getAbsoluteUrl() + build.getNumber() + "/testReport/");
            // Color
            String color = "#009933";
            if (failed > 0) {
                color = "#ff0000";
            } else if (skipped > 0) {
                color = "#ffcc00";
            }
            testAttachment.setColor(color);
            // Text
            StringBuilder message = new StringBuilder();
            message.append(String.format("%s %d test(s) passed\n", passed > 0 ? ":heavy_check_mark:" : ":x", passed));
            message.append(String.format("%s %d test(s) failed\n", failed == 0 ? ":heavy_check_mark:" : ":x", failed));
            message.append(String.format("%s %d test(s) skipped\n", skipped == 0 ? ":heavy_check_mark:" : ":x", skipped));
            testAttachment.setText(message.toString());
        }


        return payload;

    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public boolean isNotifyBackToNormalOnly() {
        return notifyBackToNormalOnly;
    }

    public void setNotifyBackToNormalOnly(boolean notifyBackToNormalOnly) {
        this.notifyBackToNormalOnly = notifyBackToNormalOnly;
    }

    public boolean isShowTestSummary() {
        return showTestSummary;
    }

    public void setShowTestSummary(boolean showTestSummary) {
        this.showTestSummary = showTestSummary;
    }
}
