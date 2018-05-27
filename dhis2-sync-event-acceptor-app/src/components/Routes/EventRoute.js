import React from "react";
import {Link} from "react-router-dom";
import axios from "axios";
import {getUrl} from "../../Constants";
import {showErrorToast} from "../../utils/ToastUtils";
import {extractAxiosError} from "../../utils/AxiosUtils";
import {Card, Spinner} from "@blueprintjs/core";
import "./EventRoute.css";
import {Doughnut} from 'react-chartjs-2';
import {Button} from "@blueprintjs/core/lib/cjs/components/button/buttons";
import EventTripsCard from "./EventTripsCard";

const STATUS_COLOR_MAP = {
    INITIALIZED: "#F9A825",
    WAITING_FOR_TEI_DATA: "#0277BD",
    WAITING_FOR_EVENT_TRANSFORMATION_DATA: "#6A1B9A",
    UPSTREAM_OFFLINE: "#455A64",
    DOWNSTREAM_OFFLINE: "#616161",
    REJECTED_BY_DOWNSTREAM: "#c62828",
    COMPLETED: "#2E7D32"
};

/**
 * @author Chathura Widanage
 */
export default class EventRoute extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            routeId: this.props.match.params.routeId,
            route: undefined,
            stats: {},
            tripState: "REJECTED_BY_DOWNSTREAM"
        }
    }

    loadRoute = () => {
        axios.get(getUrl(`eventRoutes/${this.state.routeId}`))
            .then(response => {
                this.setState({
                    route: response.data
                })
            })
            .catch(err => {
                console.error("Error in loading event route");
                showErrorToast(`Error in loading event routes : ${extractAxiosError(err)}`);
            })
    };

    loadStats = () => {
        axios.get(getUrl(`eventTrips/count?routeId=${this.state.routeId}`))
            .then(response => {
                this.setState({
                    stats: response.data
                })
            })
            .catch(err => {
                console.error("Error in loading event route counts");
                showErrorToast(`Error in loading event route counts : ${extractAxiosError(err)}`);
            })
    };

    componentDidMount() {
        this.loadRoute();
        this.loadStats();
    }

    getEdgeMarkup = (edge) => {
        return (
            <div>
                <div className="edge-info">
                    Instance<br/><em>{edge.identifier.split("_")[0]}</em>
                </div>
                <div className="edge-info">
                    Program<br/><em>{edge.dhis2InstanceProgram.displayName}</em>
                </div>
                <div className="edge-info">
                    Program Stage<br/><em>{edge.displayName}</em>
                </div>
            </div>
        );
    };

    changeTripState = (state) => {
        this.setState({
            tripState: state
        })
    };

    render() {
        if (!this.state.route) {
            return <Spinner/>
        }

        let chartData = {
            datasets: [{
                data: [], backgroundColor: []
            }],
            labels: []
        };

        Object.keys(this.state.stats).forEach(key => {
            chartData.labels.push(key);
            chartData.datasets[0].data.push(this.state.stats[key]);
            chartData.datasets[0].backgroundColor.push(STATUS_COLOR_MAP[key]);
        });

        return (
            <div>
                <ul className="pt-breadcrumbs">
                    <li><Link className="pt-breadcrumbs-collapsed" to="/"/></li>
                    <li>
                        <Link to="/routes">
                            <span className="pt-breadcrumb">Routes</span>
                        </Link>
                    </li>
                    <li><span
                        className="pt-breadcrumb pt-breadcrumb-current">{this.state.route.name || "Unnamed Route"}</span>
                    </li>
                </ul>
                <div className="event-route-info-wrapper">
                    <Card>
                        <h5>Summary</h5>
                        <table className="pt-html-table pt-html-table-striped" width="100%">
                            <tbody>
                            <tr width={400}>
                                <td>Name</td>
                                <td>{this.state.route.name || "Unnamed Route"}</td>
                            </tr>
                            <tr>
                                <td>Source</td>
                                <td>
                                    {this.getEdgeMarkup(this.state.route.source)}
                                </td>
                            </tr>
                            <tr>
                                <td>Destination</td>
                                <td>
                                    {this.getEdgeMarkup(this.state.route.destination)}
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </Card>
                    <Card>
                        <h5>Trips</h5>
                        <div className="event-route-stat-wrapper">
                            <div>
                                <Button text="Refresh" icon="refresh" onClick={this.loadStats}/>
                                <table className="pt-html-table">
                                    <thead>
                                    <tr>
                                        <td>State</td>
                                        <td>Count</td>
                                        <td>Actions</td>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {Object.keys(this.state.stats).map(key => {
                                        return (
                                            <tr key={key}>
                                                <td>{key}</td>
                                                <td>{this.state.stats[key]}</td>
                                                <td>
                                                    <Button text="Explore" small={true} onClick={() => {
                                                        this.changeTripState(key)
                                                    }}/>
                                                </td>
                                            </tr>
                                        )
                                    })}
                                    </tbody>
                                </table>
                            </div>
                            <div>
                                <Doughnut
                                    options={{
                                        maintainAspectRatio: false
                                    }}
                                    data={chartData} height={350}
                                />
                            </div>
                        </div>
                    </Card>
                    <EventTripsCard state={this.state.tripState} route={this.state.routeId}/>
                </div>
            </div>
        );
    }
}