package com.backelite.jenkins.rocketchat;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.io.Serializable;

/**
 * Created by gillesgrousset on 07/07/2016.
 */
@Extension
public class RocketChatDescriptor extends BuildStepDescriptor<Publisher> implements Serializable {

    private String webhookUrl;

    @DataBoundConstructor
    public RocketChatDescriptor() {
        super(RocketChatPublisher.class);
        load();
    }

    @Override
    public String getDisplayName() {
        return "Rocket.Chat";
    }

    @Override
    public boolean isApplicable(Class<? extends AbstractProject> aClass) {
        return true;
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {

        json = json.getJSONObject("rocketchat");
        webhookUrl = json.getString("webhookURL");
        save();
        return true;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }
}
