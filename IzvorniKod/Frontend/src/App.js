import React from 'react';
import { BrowserRouter, Switch, Route } from 'react-router-dom';
import createConference from './components/CreateConference';
import HomePage from './components/HomePage';
import Login from './components/Login';
import MyPage from './components/MyPage';
import Register from './components/Register';
import MenuPage from './components/MenuPage';
import DataGroupForm from './components/DataGroupForm';
import SpecialEventsPage from './components/SpecialEventsPage';
import SpecialEventForm from './components/SpecialEventForm';
import MultimediaFront from './components/MultimediaFront';
import showAttendees from './components/ShowAttendees'
import Gallery from './components/Gallery';
import MainAdminSearch from './components/MainAdminSearch';
import MulDateForm from './components/MulDateForm';
import ConferencePDFPage from './components/ConferencePDFPage';
import CertificatePDFPage from './components/CertificatePDFPage';

function App() {
    return (
        <BrowserRouter>
            <Switch>
                <Route exact path="/" component={HomePage} />
                <Route exact path="/login" component={Login} />
                <Route exact path="/register" component={Register} />
                <Route exact path="/createConference" component={createConference} />
                <Route exact path="/menu" component={MenuPage} />
                <Route exact path="/myConference" component={MyPage} />
                <Route exact path="/dataGroupCreate" component={DataGroupForm} />
                <Route exact path="/specialEvents" component={SpecialEventsPage} />
                <Route exact path="/createSpecialEvent" component={SpecialEventForm} />
                <Route exact path="/multimedia" component={MultimediaFront} />
                <Route exact path="/gallery" component={Gallery} />
                <Route exact path="/showAttendees/:id" component={showAttendees}/>
                <Route exact path="/showParticipants" component={MainAdminSearch}/>
                <Route exact path="/addMultimediaDate" component={MulDateForm}/>
                <Route exact path="/downloadConference" component={ConferencePDFPage}/>
                <Route exact path="/downloadCertificate" component={CertificatePDFPage}/>
            </Switch>
        </BrowserRouter>
    );
}

export default App;