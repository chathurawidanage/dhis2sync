import React from "react";
import Link from "react-router-dom/es/Link";
import {ButtonGroup, Button} from "@blueprintjs/core";
import NewEventRoutePopup from "./NewEventRoutePopup";

export default class EventRouteManager extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            newDialogVisible: false
        }
    }

    refreshRoutes = () => {

    };

    showNewRouteDialog = () => {
        this.setState({
            newDialogVisible: true
        })
    };

    onRouteAdded = () => {

    };

    closeDialog = () => {
        this.setState({
            newDialogVisible: false
        });
    }

    render() {
        return (
            <div>
                <ul className="pt-breadcrumbs">
                    <li><Link className="pt-breadcrumbs-collapsed" to="/"/></li>
                    <li><span className="pt-breadcrumb pt-breadcrumb-current">Routes</span></li>
                </ul>
                <div>
                    <ButtonGroup style={{float: 'right'}}>
                        <Button onClick={this.showNewRouteDialog} text="New Event Route"/>
                        <Button onClick={this.refreshRoutes} icon="refresh"/>
                    </ButtonGroup>
                </div>
                <NewEventRoutePopup onRouteAdded={this.onRouteAdded}
                                    onClose={this.closeDialog}
                                    isOpen={this.state.newDialogVisible}/>
                <table className="pt-html-table pt-html-table-striped" style={{width: '100%'}}>
                    <thead>
                    <tr>
                        <td>Source Instance</td>
                        <td>Source Program Stage</td>
                        <td>Destination Instance</td>
                        <td>Destination Program Stage</td>
                        <td></td>
                    </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>
        )
    }
}