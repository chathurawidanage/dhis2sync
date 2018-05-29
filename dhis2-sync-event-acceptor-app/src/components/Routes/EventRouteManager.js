import React from "react";
import Link from "react-router-dom/es/Link";
import {Button, ButtonGroup, Switch} from "@blueprintjs/core";
import NewEventRoutePopup from "./NewEventRoutePopup";
import axios from "axios";
import {getUrl} from "../../Constants";
import {showErrorToast, showSuccessToast} from "../../utils/ToastUtils";
import {extractAxiosError} from "../../utils/AxiosUtils";
import LoadingComponent from "../common/LoadingComponent";

/**
 * @author Chathura Widanage
 */
export default class EventRouteManager extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            newDialogVisible: false,
            routes: [],
            loading: false
        }
    }

    componentDidMount() {
        this.refreshRoutes();
    }

    setLoading = (loading) => {
        this.setState({loading})
    };

    refreshRoutes = () => {
        this.setLoading(true);
        axios.get(getUrl('eventRoutes')).then(response => {
            this.setState({
                routes: response.data
            });
            this.setLoading(false);
        }).catch(err => {
            showErrorToast(`Failed to load event routes : ${extractAxiosError(err)}`);
            this.setLoading(false);
        });
    };

    showNewRouteDialog = () => {
        this.setState({
            newDialogVisible: true
        })
    };

    onRouteAdded = () => {
        this.closeDialog();
        this.refreshRoutes();
    };

    closeDialog = () => {
        this.setState({
            newDialogVisible: false
        });
    };

    onSyncToggleChanged = (index) => {
        axios.post(getUrl((`eventRoutes/${this.state.routes[index].id}/toggleSync`))).then(response => {
            let routes = this.state.routes;
            routes.splice(index, 1, response.data);
            this.setState({
                routes
            });
            showSuccessToast(`Successfully ${response.data.enableSync ? 'enabled' : 'disabled'} syncing`);
        }).catch(err => {
            showErrorToast(`Failed to toggle sync status : ${extractAxiosError(err)}`);
        });
    };

    render() {
        let routes = this.state.routes.map((route, index) => {
            return (
                <tr key={index}>
                    <td>{route.name}</td>
                    <td>{route.source.identifier.split("_")[0]}</td>
                    <td>{route.source.displayName}</td>
                    <td>{route.destination.identifier.split("_")[0]}</td>
                    <td>{route.destination.displayName}</td>
                    <td>
                        <Switch checked={route.enableSync} onChange={() => {
                            this.onSyncToggleChanged(index)
                        }}/>
                    </td>
                    <td>
                        <Link to={"/routes/" + route.id} className="pt-button pt-button-primary pt-small">
                            Info
                        </Link>
                    </td>
                </tr>
            )
        });

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
                <LoadingComponent loading={this.state.loading}>
                    <table className="pt-html-table pt-html-table-striped" style={{width: '100%'}}>
                        <thead>
                        <tr>
                            <td>Name</td>
                            <td>Source Instance</td>
                            <td>Source Program Stage</td>
                            <td>Destination Instance</td>
                            <td>Destination Program Stage</td>
                            <td>Status</td>
                            <td>Actions</td>
                        </tr>
                        </thead>
                        <tbody>
                        {routes}
                        </tbody>
                    </table>
                </LoadingComponent>
            </div>
        )
    }
}