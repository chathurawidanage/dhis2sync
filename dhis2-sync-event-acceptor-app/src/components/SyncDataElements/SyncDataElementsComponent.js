import React from "react";
import axios from "axios";
import {getUrl} from "../../Constants";
import NewSyncDataElementPopup from "./NewSyncDataElementPopup";
import {Button} from "@blueprintjs/core";
import {showErrorToast} from "../../utils/ToastUtils";
import {extractAxiosError} from "../../utils/AxiosUtils";
import {Link} from "react-router-dom";

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
        axios.get(`${getUrl('syncDataElements')}`)
            .then(response => {
                this.setState({
                    syncDataElements: response.data
                });
            })
            .catch(err => {
                console.error(err);
                showErrorToast(extractAxiosError(err));
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
                <td>{index + 1}</td>
                <td>{de.code}</td>
                <td>{de.displayName}</td>
            </tr>
        });

        return (
            <div>
                <ul className="pt-breadcrumbs">
                    <li><Link className="pt-breadcrumbs-collapsed" to="/"/></li>
                    <li><span className="pt-breadcrumb pt-breadcrumb-current">Sync Data Elements</span></li>
                </ul>
                <div>
                    <Button onClick={this.showNewDialog} text="New Sync Data Element" style={{float: 'right'}}/>
                </div>

                <NewSyncDataElementPopup onElementAdded={this.onElementAdded}
                                         onClose={this.closeDialog}
                                         isOpen={this.state.newDialogVisible}/>

                <table className="pt-html-table pt-html-table-striped" style={{width: '100%'}}>
                    <thead>
                    <tr>
                        <th>Index</th>
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