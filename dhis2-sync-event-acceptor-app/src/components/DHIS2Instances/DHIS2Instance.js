import React from "react";
import axios from "axios";
import {getUrl} from "../../Constants";
import {Link} from "react-router-dom";
import {Button, Card, Elevation, Icon, Intent, Spinner, Tab, Tabs} from "@blueprintjs/core";
import "./DHIS2Instance.css";
import DHIS2InstanceProgramStages from "./DHIS2InstanceProgramStages";
import DHIS2InstanceDataElements from "./DHIS2InstanceDataElements";
import {showErrorToast, showSuccessToast} from "../../utils/ToastUtils";
import {extractAxiosError} from "../../utils/AxiosUtils";

/**
 * @author Chathura Widanage
 */
export default class DHIS2Instance extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            instanceId: this.props.match.params.instanceId,
            instance: undefined,
            syncingMetaData: false,
            selectedMetadataTab: "ps"
        }
    }

    componentDidMount() {
        console.log(this.state);
        this.refreshInstance();
    }

    refreshInstance = () => {
        axios.get(`${getUrl(`dhis2Instances/${this.state.instanceId}`)}`)
            .then(response => {
                console.log("R", response);
                this.setState({
                    instance: response.data
                })
            })
            .catch(err => {
                console.error("Error in fetching dhis2 instances", err);
            });
    };

    setSyncingMetaData = (syncing) => {
        this.setState({
            syncingMetaData: syncing
        });
    };

    onSyncMetaClicked = () => {
        this.setSyncingMetaData(true);
        axios.post(`${getUrl(`dhis2Instances/${this.state.instanceId}/syncMetadata`)}`)
            .then(response => {
                this.setState({
                    instance: response.data
                });
                this.setSyncingMetaData(false);
                showSuccessToast("Metadata Synchronized");
            })
            .catch(err => {
                console.error("Error in syncing meta data", err);
                this.setSyncingMetaData(false);
                showErrorToast(`Metadata synchronization failed : ${extractAxiosError(err)}`);
            });
    };

    onMetaDataTabChanged = (tab) => {
        this.setState({
            selectedMetadataTab: tab
        })
    };

    onSyncStatusToggleClicked = () => {
        axios.post(`${getUrl(`dhis2Instances/${this.state.instanceId}/${this.state.instance.syncEnabled ? 'stop' : 'start'}`)}`)
            .then(response => {
                this.setState({
                    instance: {...this.state.instance, syncEnabled: !this.state.instance.syncEnabled}
                }, () => {
                    showSuccessToast(`Instance Syncing ${this.state.instance.syncEnabled ? 'started.' : 'stopped.'}`);
                });
            })
            .catch(err => {
                console.error("Error in toggling sync status", err);
                showErrorToast(`Failed : ${err.message}`);
            });
    };

    render() {
        if (!this.state.instance) {
            return <Spinner/>;
        }
        return (
            <div>
                <ul className="pt-breadcrumbs">
                    <li><Link className="pt-breadcrumbs-collapsed" to="/"/></li>
                    <li>
                        <Link to="/dhis2Instances">
                            <span className="pt-breadcrumb">DHIS2 Instances</span>
                        </Link>
                    </li>
                    <li><span className="pt-breadcrumb pt-breadcrumb-current">{this.state.instanceId}</span></li>
                </ul>
                <div className="dhis2-instance-summary-wrapper">
                    <Card ielevation={Elevation.TWO}>
                        <h5>Summary</h5>
                        <table
                            className="pt-html-table pt-html-table-striped" width="100%">
                            <tbody>
                            <tr>
                                <td>Identifier</td>
                                <td>{this.state.instance.id}</td>
                            </tr>
                            <tr>
                                <td>URL</td>
                                <td>{this.state.instance.url}</td>
                            </tr>
                            <tr>
                                <td>Description</td>
                                <td>{this.state.instance.description}</td>
                            </tr>
                            <tr>
                                <td>Metadata Synchronized</td>
                                <td>
                                    {this.state.instance.metaDataSynced &&
                                    <Icon icon="tick-circle" intent={Intent.SUCCESS}/>}
                                    {!this.state.instance.metaDataSynced &&
                                    <Icon icon="ban-circle" intent={Intent.DANGER}/>}
                                </td>
                            </tr>
                            <tr>
                                <td>No of Program Stages exposed</td>
                                <td>{this.state.instance.programStages.length}</td>
                            </tr>
                            <tr>
                                <td>No of Data Elements exposed</td>
                                <td>{this.state.instance.dataElements.length}</td>
                            </tr>
                            <tr>
                                <td>Sync Status</td>
                                <td>{this.state.instance.syncEnabled ? 'STARTED' : 'STOPPED'}</td>
                            </tr>
                            </tbody>
                        </table>
                        <div className="dhis2-instance-operations">
                            <Button onClick={this.onSyncStatusToggleClicked} disabled={this.state.syncingMetaData}
                                    intent={this.state.instance.syncEnabled ? Intent.DANGER : Intent.SUCCESS}
                                    icon={this.state.instance.syncEnabled ? "stop" : "play"}>
                                {this.state.instance.syncEnabled ? 'Stop' : 'Start'} Syncing
                            </Button>
                            <Button onClick={this.onSyncMetaClicked} disabled={this.state.syncingMetaData}
                                    icon="refresh">
                                Sync Metadata
                            </Button>
                        </div>
                    </Card>
                </div>
                <div>
                    <Card ielevation={Elevation.TWO}>
                        <h5>Metadata</h5>
                        <Tabs id="MetaDataTabs" selectedTabId={this.state.selectedMetadataTab}
                              onChange={this.onMetaDataTabChanged}>
                            <Tab id="ps" title="Program Stages"
                                 panel={<DHIS2InstanceProgramStages
                                     programStages={this.state.instance.programStages}/>}/>
                            <Tab id="de" title="Data Elements"
                                 panel={<DHIS2InstanceDataElements dataElements={this.state.instance.dataElements}/>}/>
                        </Tabs>
                    </Card>
                </div>
            </div>
        );
    }
}