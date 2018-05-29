import React from "react";
import {Button, ButtonGroup} from "@blueprintjs/core";
import axios from "axios";
import {getUrl} from "../../Constants";
import NewDHIS2InstancePopup from "./NewDHIS2InstancePopup";
import {Link} from "react-router-dom";
import {showErrorToast} from "../../utils/ToastUtils";
import {extractAxiosError} from "../../utils/AxiosUtils";
import LoadingComponent from "../common/LoadingComponent";

/**
 * @author Chathura Widanage
 */
export default class DHIS2InstancesManager extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            instances: [],
            newDialogVisible: false,
            loading: false
        }
    }

    componentDidMount() {
        this.refreshInstances();
    }

    setLoading = (loading) => {
        this.setState({loading})
    };

    refreshInstances = () => {
        this.setLoading(true)
        axios.get(`${getUrl('dhis2Instances')}`)
            .then(response => {
                this.setState({
                    instances: response.data
                });
                this.setLoading(false);
            })
            .catch(err => {
                console.error("Error in fetching dhis2 instances", err);
                showErrorToast(`Error in fetching dhis2 instances : ${extractAxiosError(err)}`)
                this.setLoading(false)
            })
    };

    onInstanceAdded = (instance) => {
        this.closeDialog();
        this.refreshInstances();
    };

    closeDialog = () => {
        this.setState({
            newDialogVisible: false
        })
    };

    showNewDialog = () => {
        this.setState({
            newDialogVisible: true
        })
    };

    render() {
        return (
            <div>
                <ul className="pt-breadcrumbs">
                    <li><Link className="pt-breadcrumbs-collapsed" to="/"/></li>
                    <li><span className="pt-breadcrumb pt-breadcrumb-current">DHIS2 Instances</span></li>
                </ul>
                <div>
                    <ButtonGroup style={{float: 'right'}}>
                        <Button onClick={this.showNewDialog} text="New DHIS2 Instance"/>
                        <Button onClick={this.refreshInstances} icon="refresh"/>
                    </ButtonGroup>
                </div>

                <NewDHIS2InstancePopup onInstanceAdded={this.onInstanceAdded}
                                       onClose={this.closeDialog}
                                       isOpen={this.state.newDialogVisible}/>
                <LoadingComponent loading={this.state.loading}>
                    <table className="pt-html-table pt-html-table-striped" style={{width: '100%'}}>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>URL</th>
                            <th>Description</th>
                            <th>Sync Status</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {
                            this.state.instances && this.state.instances.map(instance => {
                                return <tr key={instance.id}>
                                    <td>{instance.id}</td>
                                    <td>{instance.url}</td>
                                    <td>{instance.description}</td>
                                    <td>{instance.syncEnabled ? "STARTED" : "STOPPED"}</td>
                                    <td>
                                        <Link to={"/dhis2Instances/" + instance.id}
                                              className="pt-button pt-icon-cog pt-small">
                                            Configure
                                        </Link>
                                    </td>
                                </tr>
                            })
                        }
                        </tbody>
                    </table>
                </LoadingComponent>
            </div>
        );
    }
}