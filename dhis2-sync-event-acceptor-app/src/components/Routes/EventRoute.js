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
    SCHEDULED_FOR_PROCESSING: "#EF6C00",
    WAITING_FOR_TEI_DATA: "#0277BD",
    UPSTREAM_OFFLINE: "#455A64",
    DOWNSTREAM_OFFLINE: "#616161",
    WAITING_FOR_EVENT_TRANSFORMATION_DATA: "#6A1B9A",
    REJECTED_BY_DOWNSTREAM: "#ef5350",
    ERROR_IN_HANDLING_TRIP: "#c62828",
    TIME_OUT_PROCESSING: "#AD1457",
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
            loadingRoute: false,
            route: undefined,
            stats: {},
            loadingStats: false,
            tripState: "REJECTED_BY_DOWNSTREAM"
        }
        this.pollingTimer = -1;
    }

    setLoadingRoute = (loadingRoute) => {
        this.setState({loadingRoute});
    };

    setLoadingStats = (loadingStats) => {
        this.setState({loadingStats});
    };

    loadRoute = () => {
        this.setLoadingRoute(true);
        axios.get(getUrl(`eventRoutes/${this.state.routeId}`))
            .then(response => {
                this.setState({
                    route: response.data
                });
                this.setLoadingRoute(false);
            })
            .catch(err => {
                this.setLoadingRoute(false);
                console.error("Error in loading event route");
                showErrorToast(`Error in loading event routes : ${extractAxiosError(err)}`);
            })
    };

    loadStats = () => {
        this.setLoadingStats(true);
        axios.get(getUrl(`eventTrips/count?routeId=${this.state.routeId}`))
            .then(response => {
                this.setState({
                    stats: response.data
                });
                this.setLoadingStats(false);
            })
            .catch(err => {
                console.error("Error in loading event route counts");
                showErrorToast(`Error in loading event route counts : ${extractAxiosError(err)}`);
                this.setLoadingStats(false);
            })
    };

    componentDidMount() {
        this.loadRoute();
        this.loadStats();
        this.pollingTimer = setInterval(this.loadStats, 5000);
    }

    componentWillUnmount() {
        clearTimeout(this.pollingTimer);
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

        Object.keys(STATUS_COLOR_MAP).forEach(key => {
            chartData.labels.push(key);
            chartData.datasets[0].data.push(this.state.stats[key] || 0);
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
                    {
                        Object.keys(this.state.stats).length > 0 &&
                        <div>
                            <Card>
                                <h5>Trips</h5>
                                <div className="event-route-stat-wrapper">
                                    <div>
                                        <Button text="Refresh" icon="refresh" onClick={this.loadStats}
                                                loading={this.state.loadingStats}/>
                                        <table className="pt-html-table">
                                            <thead className="text-bold">
                                            <tr>
                                                <td></td>
                                                <td>State</td>
                                                <td width={200}>Count</td>
                                                <td>Actions</td>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            {Object.keys(STATUS_COLOR_MAP).map(key => {
                                                return (
                                                    <tr key={key}>
                                                        <td style={{
                                                            backgroundColor: STATUS_COLOR_MAP[key]
                                                        }}/>
                                                        <td>{key}</td>
                                                        <td>{this.state.stats[key] || 0}</td>
                                                        <td>
                                                            <Button text="Explore" small={true} icon="eye-open"
                                                                    onClick={() => {
                                                                        this.changeTripState(key)
                                                                    }}/>
                                                            {/*<Button text="Reinitialize" small={true} icon="repeat"*/}
                                                            {/*onClick={() => {*/}
                                                            {/*this.changeTripState(key)*/}
                                                            {/*}}/>*/}
                                                        </td>
                                                    </tr>
                                                )
                                            })}
                                            <tr className="text-bold trips-summary-total">
                                                <td></td>
                                                <td>TOTAL</td>
                                                <td>
                                                    {Object.values(this.state.stats).reduce((acc, val) => {
                                                        return acc + val;
                                                    }, 0)}
                                                </td>
                                                <td/>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div>
                                        <Doughnut
                                            options={{
                                                legend: false,
                                                maintainAspectRatio: false,
                                                animation: {
                                                    duration: 0
                                                }
                                            }}
                                            data={chartData} height={350}
                                        />
                                    </div>
                                </div>
                            </Card>
                            <EventTripsCard state={this.state.tripState} route={this.state.routeId}/>
                        </div>
                    }
                    {
                        Object.keys(this.state.stats).length === 0 &&
                        <Card>
                            <h5>Trips</h5>
                            <p className="text-center">No trips in this route yet</p>
                        </Card>
                    }
                </div>
            </div>
        );
    }
}