import React from "react";
import axios from "axios";
import {SERVER_URL} from "../../Constants";
import NewSyncDataElementPopup from "./NewSyncDataElementPopup";
import {Button} from "@blueprintjs/core";

export class SyncDataElementsComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            syncDataElements: [],
            newDialogVisible: false
        };
    }

    componentDidMount() {
        this.refreshDataElements();
    }

    refreshDataElements = () => {
        axios.get(`${SERVER_URL}syncDataElements`)
            .then(response => {
                this.setState({
                    syncDataElements: response.data
                })
            })
            .catch(err => {
                console.error(err);
            })
    };

    showNewDialog = () => {
        this.setState({
            newDialogVisible: true
        })
    };

    closeDialog = () => {
        this.setState({
            newDialogVisible: false
        });
    };

    onElementAdded = () => {
        this.closeDialog();
        this.refreshDataElements();
    };


    render() {
        let deRows = this.state.syncDataElements.map((de, index) => {
            return <tr key={index}>
                <td>{index}</td>
                <td>{de.code}</td>
                <td>{de.displayName}</td>
            </tr>
        });

        return (
            <div>
                <h2>Sync Data Elements</h2>
                <div>
                    <Button onClick={this.showNewDialog} text="New Sync Data Element" style={{float: 'right'}}/>
                </div>

                <NewSyncDataElementPopup onElementAdded={this.onElementAdded}
                                         onClose={this.closeDialog}
                                         isOpen={this.state.newDialogVisible}/>

                <table className="pt-html-table pt-html-table-striped" style={{width: '100%'}}>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Code</th>
                        <th>Data Element Name</th>
                    </tr>
                    </thead>
                    <tbody>
                    {deRows}
                    </tbody>
                </table>
            </div>
        )
    }
}