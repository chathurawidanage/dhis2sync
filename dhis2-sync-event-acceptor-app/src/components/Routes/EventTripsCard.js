import React from "react";
import {Card} from "@blueprintjs/core";
import axios from "axios";
import {getUrl} from "../../Constants";
import {showErrorToast, showSuccessToast} from "../../utils/ToastUtils";
import {extractAxiosError} from "../../utils/AxiosUtils";
import {Button, ButtonGroup} from "@blueprintjs/core";
import LoadingComponent from "../common/LoadingComponent";

export default class EventTripsCard extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            loading: false,
            trips: [],
            number: 0,
            totalPages: 1,
            reinitializingTrips: false
        };
    }

    setReinitializingTrips = (reinitializing) => {
        this.setState({
            reinitializingTrips: reinitializing
        })
    };

    componentDidMount() {
        this.loadTrips();
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.state !== this.props.state) {
            this.setState({
                number: 0,
                totalPages: 1
            });
            this.loadTrips(nextProps);
        }
    }

    loadTrips = (props = this.props) => {
        console.log(props, this.props);
        this.setState({loading: true, trips: []});
        axios.get(getUrl(`eventTrips?page=${this.state.number}&routeId=${props.route}&status=${props.state}`))
            .then(response => {
                let {totalPages, content, number} = response.data;
                this.setState({totalPages, content, number, trips: content, loading: false});
            })
            .catch(err => {
                console.error("Error in loading trips", err);
                showErrorToast(`Error in loading trips : ${extractAxiosError(err)}`);
                this.setState({loading: false})
            });
    };

    reInitializeTrip = (tripId) => {
        axios.post(getUrl(`eventTrips/${tripId}/reinitialize`))
            .then(response => {
                showSuccessToast("Trip reinitialized successfully");
                this.loadTrips();
            })
            .catch(err => {
                console.log("Error in reinitializing", err);
                showErrorToast(`Error in reinitializing trip : ${extractAxiosError(err)}`);
            })
    };

    reInitializeAllTrips = () => {
        this.setReinitializingTrips(true);
        axios.post(getUrl(`eventTrips/reinitialize?routeId=${this.props.route}&status=${this.props.state}`))
            .then(response => {
                showSuccessToast("Trips reinitialized successfully");
                this.setReinitializingTrips(false);
                this.loadTrips();
            })
            .catch(err => {
                console.log("Error in reinitializing", err);
                showErrorToast(`Error in reinitializing trips with state ${this.props.state}: ${extractAxiosError(err)}`);
                this.setReinitializingTrips(false);
            })
    };

    loadNextPage = () => {
        if (this.state.number < this.state.totalPages - 1) {
            this.setState({
                number: this.state.number + 1
            }, this.loadTrips)
        }
    };

    loadPreviousPage = () => {
        if (this.state.number > 0) {
            this.setState({
                number: this.state.number - 1
            }, this.loadTrips)
        }
    };

    render() {
        return (
            <Card>
                <h5>{this.props.state}</h5>
                <LoadingComponent loading={this.state.loading}>
                    <div className="pt-align-right text-right">
                        <ButtonGroup>
                            <Button text="Reinitialize All" onClick={this.reInitializeAllTrips} icon="repeat"
                                    loading={this.state.reinitializingTrips}/>
                            <Button text="Refresh" onClick={() => {
                                this.loadTrips()
                            }} icon="refresh"/>
                            <Button icon="chevron-left" disabled={this.state.number < 1}
                                    onClick={this.loadPreviousPage}/>
                            <Button icon="chevron-right" disabled={this.state.number >= this.state.totalPages - 1}
                                    onClick={this.loadNextPage}/>
                        </ButtonGroup>
                    </div>
                    <div className="pt-align-right text-right" style={{marginTop: 5}}>
                        Showing Page {this.state.number + 1} of {this.state.totalPages}
                    </div>
                    <table className="pt-html-table pt-html-table-striped pt-fill" width="100%">
                        <thead>
                        <tr>
                            <td>
                                ID
                            </td>
                            <td>
                                Previous State
                            </td>
                            <td>
                                Current State
                            </td>
                            <td>
                                State Message
                            </td>
                            <td>
                                Last Updated time
                            </td>
                            <td>
                                Action
                            </td>
                        </tr>
                        </thead>
                        <tbody>
                        {this.state.trips.map(trip => {
                            return (
                                <tr key={trip.id}>
                                    <td>{trip.id}</td>
                                    <td>{trip.latestTransformation.previousStatus}</td>
                                    <td>{trip.latestTransformation.currentStatus}</td>
                                    <td>{trip.latestTransformation.message}</td>
                                    <td>{new Date(trip.lastUpdate).toTimeString()}</td>
                                    <td>
                                        <Button text="Reinitialize" small={true} onClick={() => {
                                            this.reInitializeTrip(trip.id);
                                        }}/>
                                    </td>
                                </tr>
                            )
                        })}
                        </tbody>
                    </table>
                    {this.state.trips && this.state.trips.length === 0 &&
                    <p className="text-center">No event trips in this category</p>}
                </LoadingComponent>
            </Card>
        )
    }
}