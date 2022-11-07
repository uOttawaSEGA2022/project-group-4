package com.example.mealer_project.data.models.inbox;

import com.example.mealer_project.app.App;
import com.example.mealer_project.data.entity_models.ComplaintEntityModel;
import com.example.mealer_project.data.sources.actions.InboxActions;

import java.util.ArrayList;
import java.util.List;

public class SampleComplaintsData {

    public void createSampleComplaints(int size) {
        List<ComplaintEntityModel> complaints = new ArrayList<>();

        String[] chefIds = new String[] {
                "hPexzu45xjVBChIHuCgoHzn8vzY2",
                "rZfIpemX3CXQiqmc4t7KBpoYAuk1",
                "dNGgtlXhSrW9dnOZ7GNxUiexK5s2",
                "RiKxdx9zZNS8BmLBOFsA5W9oxgM2",
                "KcBOX85rKwfBO6WmrTITYyTYuH23",
                "rZfIpemX3CXQiqmc4t7KBpoYAuk1",
        };

        String[] clientIds = new String[] {
                "09byC6VCX3Qi3AvKG6NDEDypewO2",
                "9RFY3Qmge2V1iJ3TWKp4hTA6GHp2"
        };

//        private String title;
//        private String description;
//        private String clientId;
//        private String chefId;
//        private String dateSubmitted;

        complaints.add(new ComplaintEntityModel("", "", "", "", "", ""));


    }

    private void addComplaint(String title, String desc, String dateSubmitted) {
//        App.getPrimaryDatabase().INBOX.addComplaint();
    }

    public static void main(String[] args) {


    }

}
