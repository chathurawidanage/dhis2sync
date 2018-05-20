package com.cwidanage.dhis2.common.services.dhis2;

import com.cwidanage.dhis2.common.models.dhis2.TrackedEntityInstance;
import com.cwidanage.dhis2.common.models.sync.DHIS2Instance;
import com.cwidanage.dhis2.common.models.sync.TrackedEntityInstanceIdentifier;
import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceTrackedEntityInstance;
import com.cwidanage.dhis2.common.repositories.dhis2.DHIS2TrackedEntityInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Chathura Widanage
 */
@Service
public class DHIS2TrackedEntityInstanceService {

    @Autowired
    private DHIS2TrackedEntityInstanceRepository repository;

    public DHIS2InstanceTrackedEntityInstance findByTEIId(DHIS2Instance dhis2Instance, String trackedEntityInstanceId) {
        return this.repository.findOne(generateIdentifier(dhis2Instance, trackedEntityInstanceId));
    }

    public DHIS2InstanceTrackedEntityInstance findByAttributeValue(DHIS2Instance dhis2Instance, String attributeValue) {
        return this.repository.findDistinctByDhis2InstanceAndTrackedEntityInstanceIdentifier_Id(dhis2Instance,
                attributeValue);
    }

    public DHIS2InstanceTrackedEntityInstance save(DHIS2InstanceTrackedEntityInstance dhis2InstanceTrackedEntityInstance) {
        return this.repository.save(dhis2InstanceTrackedEntityInstance);
    }

    protected static String generateIdentifier(DHIS2Instance dhis2Instance, String trackedEntityInstanceId) {
        return String.format("%s_%s", dhis2Instance.getId(), trackedEntityInstanceId);
    }

    public DHIS2InstanceTrackedEntityInstance createAndSave(DHIS2Instance dhis2Instance,
                                                            TrackedEntityInstance trackedEntityInstance,
                                                            TrackedEntityInstanceIdentifier trackedEntityInstanceIdentifier) {
        return this.save(
                createDHIS2TrackedEntityInstance(dhis2Instance, trackedEntityInstance, trackedEntityInstanceIdentifier)
        );
    }

    public static DHIS2InstanceTrackedEntityInstance createDHIS2TrackedEntityInstance(DHIS2Instance dhis2Instance,
                                                                                      TrackedEntityInstance trackedEntityInstance,
                                                                                      TrackedEntityInstanceIdentifier trackedEntityInstanceIdentifier) {
        DHIS2InstanceTrackedEntityInstance d2TEI = new DHIS2InstanceTrackedEntityInstance();
        d2TEI.setDhis2Instance(dhis2Instance);
        d2TEI.setTrackedEntityInstanceIdentifier(trackedEntityInstanceIdentifier);
        d2TEI.setOrgUnit(trackedEntityInstance.getOrgUnit());
        d2TEI.setTrackedEntity(trackedEntityInstance.getTrackedEntity());
        d2TEI.setTrackedEntityInstance(trackedEntityInstance.getTrackedEntityInstance());
        d2TEI.setIdentifier(generateIdentifier(dhis2Instance, trackedEntityInstance.getTrackedEntityInstance()));
        return d2TEI;
    }
}
