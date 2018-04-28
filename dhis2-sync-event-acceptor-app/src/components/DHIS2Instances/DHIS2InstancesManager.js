import React from "react";
import {Button, ButtonGroup} from "@blueprintjs/core";
import axios from "axios";
import {getUrl, SERVER_URL} from "../../Constants";
import NewDHIS2InstancePopup from "./NewDHIS2InstancePopup";

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
                <h2>DHIS2 Instances</h2>
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
                    </tr>
                    </thead>
                    <tbody>
                    {
                        this.state.instances && this.state.instances.map(instance => {
                            return <tr key={instance.id}>
                                <td>{instance.id}</td>
                                <td>{instance.url}</td>
                                <td>{instance.description}</td>
                            </tr>
                        })
                    }
                    </tbody>
                </table>
            </div>
        );
    }
}