import React from "react";
import {Button, ButtonGroup} from "@blueprintjs/core";
import axios from "axios";
import {getUrl} from "../../Constants";
import NewDHIS2InstancePopup from "./NewDHIS2InstancePopup";
import {Link} from "react-router-dom";

/**
 * @author Chathura Widanage
 */
export default class DHIS2InstancesManager extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            instances: [],
            newDialogVisible: false
        }
    }

    componentDidMount() {
        this.refreshInstances();
    }

    refreshInstances = () => {
        axios.get(`${getUrl('dhis2Instances')}`)
            .then(response => {
                this.setState({
                    instances: response.data
                })
            })
            .catch(err => {
                console.error("Error in fetching dhis2 instances", err);
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

                <table className="pt-html-table pt-html-table-striped" style={{width: '100%'}}>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>URL</th>
                        <th>Description</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        this.state.instances && this.state.instances.map(instance => {
                            return <tr key={instance.id}>
                                <td>{instance.id}</td>
                                <td>{instance.url}</td>
                                <td>{instance.description}</td>
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
            </div>
        );
    }
}