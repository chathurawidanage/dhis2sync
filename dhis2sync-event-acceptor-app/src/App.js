import React, {Component} from 'react';
import "./style/app.css";
import {
    Navbar,
    NavbarDivider,
    NavbarGroup,
    Button,
    NavbarHeading,
    Alignment,
    Menu,
    MenuItem,
    MenuDivider
} from "@blueprintjs/core";
import {SyncDataElementsComponent} from "./components/SyncDataElements/SyncDataElementsComponent";
import {Link, Route} from "react-router-dom";
import DHIS2InstancesManager from "./components/DHIS2Instances/DHIS2InstancesManager";

class App extends Component {
    render() {
        return (
            <div className="app">
                <Navbar>
                    <NavbarGroup align={Alignment.LEFT}>
                        <NavbarHeading>DHIS2 - Sync</NavbarHeading>
                        <NavbarDivider/>
                        <Button className="pt-minimal" icon="home" text="Home"/>
                        <Button className="pt-minimal" icon="document" text="Files"/>
                    </NavbarGroup>
                </Navbar>
                <div className="app-content">
                    <Menu className="pt-elevation-1 app-content-sidebar">
                        <Link to="/dataElements">
                            <MenuItem icon="new-text-box" text="Data Elements"/>
                        </Link>
                        <Link to="/dhis2Instances">
                            <MenuItem icon="new-object" onClick={this.handleClick} text="DHIS2 Instances"/>
                        </Link>
                        <MenuItem icon="new-link" onClick={this.handleClick} text="New link"/>
                        <MenuDivider/>
                        <MenuItem text="Settings..." icon="cog"/>
                    </Menu>
                    <div className="app-content-ui">
                        <Route path="/dataElements" component={SyncDataElementsComponent}/>
                        <Route path="/dhis2Instances" component={DHIS2InstancesManager}/>
                    </div>
                </div>
                <div className="app-footer">
                    &copy; HISP Sri Lanka
                </div>
            </div>
        );
    }
}

export default App;
