import React from 'react';
import { Link } from 'react-router-dom';
import { Grid, Table, Header, Button, Divider, CommentContent} from 'semantic-ui-react';
import DataGroupPopup from './DataGroupPopup';
import '../styles/DataGroupPopUp.css';
import '../styles/DataGroup.css'
import '../styles/SpecialEvent.css'
import { FaPlusSquare } from 'react-icons/fa';
import DataGroupsList from './DataGroupsList';
import MainAdminSearch from './MainAdminSearch';
import axios from 'axios';
import {BsCloudSunFill} from 'react-icons/bs'
import {  Form} from 'semantic-ui-react';
import jsPDF from 'jspdf'
import {FaRegUserCircle} from 'react-icons/fa'
import Document from './ConferenceDocument'
import { pdf } from '@react-pdf/renderer';
import { saveAs } from "file-saver";

//import { font } from "./font";
//import '../utils/arial-unicode-ms-normal'

export default function MyPage() {   
    const data = {}
    const [conference, setConference] = React.useState([]);
    const [mandatoryDataGroups, setMandatoryDataGroups] = React.useState([]);
    const [weather, setWeather] = React.useState([]);
    const [form, setForm] = React.useState({path:''});
    const [formConference, setFormConference] = React.useState({path:''});
    const [operativeAdmins, setOperativeAdmins] = React.useState([]);
    const [participants, setParticipants] = React.useState([]);
    const [specialEvents, setSpecialEvents] = React.useState([]);
    const [attendees, setAttendees] = React.useState([])
    const [dataGroups, setDataGroups] = React.useState([]);

    var user = JSON.parse(localStorage.getItem('user'));
    const token = JSON.parse(localStorage.getItem('token'));

    const config= {
        headers: { Authorization : `Bearer ${token.token}`}
    }

    React.useEffect(() => {
        axios.get(`/api/myConference/${user.idUserAccount}`,
                    config)
        .then(response => {setConference(response.data); console.log(response.data)})
    }, []);

    React.useEffect(() => {
            axios.get(`/api/mandatorySet/${user.conferenceId}`,
                        config)
            .then(response =>  setMandatoryDataGroups(response.data))
            
    }, []);

    React.useEffect(() => {
        axios.get(`/api/weather/${user.conferenceId}`,
                    config)
        .then(response =>  setWeather(response.data))
    }, []);

    React.useEffect(() => {
        axios.get(`/api/myConference/${user.conferenceId}/operative`,
                    config)
        .then(response => {setOperativeAdmins(response.data); console.log(response.data)})
    }, []);

    React.useEffect(() => {
        axios.get(`/api/myConference/${user.conferenceId}/participants`,
                    config)
        .then(response => {setParticipants(response.data);})
    }, []);

    React.useEffect(() => {
        axios.get(`/api/get/${user.conferenceId}`,
                    config)
        .then(response => {setSpecialEvents(response.data);})
    }, []);

    React.useEffect(() => {
        axios.get(`/api/dataGroup/${user.conferenceId}`,
                    config)
        .then(response => {setDataGroups(response.data);})
    }, []);


function onChange(event) {
    const {name, value} = event.target;
    setForm(oldForm => ({...oldForm, [name]: value}))
    console.log(event.target.value)
}

const generatePDF = () => {
    var doc = new jsPDF('p', 'pt');
    
    /*doc.addFileToVFS("arial-unicode-ms-normal.ttf", font);
    doc.addFont("arial-unicode-ms-normal.ttf", "arial-unicode-ms", "normal");
    doc.setFont("arial-unicode-ms");*/

    doc.text(250, 20, conference.name)
    //doc.addFont('normal')
    //doc.setFont('arial-unicode-ms');
    doc.setFontSize(12)
    var height = doc.internal.pageSize.height;

    var y = 20;
    if((y+40) > height) {
        doc.addPage();
        y = 20;
    } else {
        y = y + 40;
    }
    doc.text(20, y, 'Conference: ' + conference.name)

    if((y+40) > height) {
        doc.addPage();
        y = 20;
    } else {
        y = y + 40;
    }
    doc.text(20, y, 'City: ' + conference.city)

    if((y+40) > height) {
        doc.addPage();
        y = 20;
    } else {
        y = y + 40;
    }
    doc.text(20, y, 'Description: ' + conference.description)

    if((y+40) > height) {
        doc.addPage();
        y = 20;
    } else {
        y = y + 40;
    }
    doc.text(20, y, 'Starts: ' + String(conference.dateStart).substring(0, 10))


    if((y+40) > height) {
        doc.addPage();
        y = 20;
    } else {
        y = y + 40;
    }
    doc.text(20, y, 'Ends: ' + String(conference.dateEnd).substring(0, 10))

    if((y+40) > height) {
        doc.addPage();
        y = 20;
    } else {
        y = y + 40;
    }
    doc.text(20, y, 'Topics: ' + conference.topics)

    if((y+40) > height) {
        doc.addPage();
        y = 20;
    } else {
        y = y + 40;
    }
    doc.text(20, y, 'Conference owner: Jose Mourinho')

    if((y+40) > height) {
        doc.addPage();
        y = 20;
    } else {
        y = y + 40;
    }
    doc.text(20, y, 'Main admin of the conference: ' + user.firstAndLastName)

    if((y+40) > height) {
        doc.addPage();
        y = 20;
    } else {
        y = y + 40;
    }
    doc.text(20, y, 'Operative admins:')

    var i = 0;
    operativeAdmins.map(admin => {
        if((y+40) > height) {
            doc.addPage();
            y = 20;
        } else {
            y = y + 40;
        }
        doc.text(40, y, admin.firstAndLastName);
    });
    if((y+40) > height) {
        doc.addPage();
        y = 20;
    } else {
        y = y + 40;
    }
    doc.text(20, y, 'Participants:')

    participants.map(p => {
        if((y+40) > height) {
            doc.addPage();
            y = 20;
        } else {
            y = y + 40;
        }
        doc.text(40, y, p.firstAndLastName);
    });

    if((y+40) > height) {
        doc.addPage();
        y = 20;
    } else {
        y = y + 40;
    }
    doc.text(20, y, 'Special events:')

    //var attendees = [];
    specialEvents.map(event => {
        if((y+40) > height) {
            doc.addPage();
            y = 20;
        } else {
            y = y + 40;
        }
        doc.text(40, y, event.type + ' (' + event.capacity + ')')
        /*axios.get(`/specialEvents/attending/${event.idSpecialEvent}`, config).then(response => response.data.forEach(element => {
            attendees.push(element)
        }))
        attendees.map(a => {doc.text(80, 340 + i*40, a.firstAndLastName); ++i}) */
    })

    if((y+40) > height) {
        doc.addPage();
        y = 20;
    } else {
        y = y + 40;
    }
    doc.text(20, y, 'Data groups:')

    
    var j = 1;
    dataGroups.map(group => {
        if((y+40) > height) {
            doc.addPage();
            y = 20;
        } else {
            y = y + 40;
        }
        doc.text(40, y, j + '. '+ group.groupName);
         ++j
    });


    doc.save('demo.pdf')
}

const generateCertificate = () => {
    var doc = new jsPDF('p', 'pt');
      
    doc.text(220, 20, 'Participation certificate')
    doc.setFont('arial-unicode-ms');
    //doc.addFont('normal')
    doc.setFontSize(12)
    doc.text(20, 60, 'This certifies that')
    doc.text(20, 100, user.firstAndLastName)
    doc.text(20, 140, user.country + ', ' + user.companyName)
    doc.text(20, 180, 'has attended the conference ' + conference.name)
    doc.text(20, 220, conference.city + ', ' + conference.description)
    doc.text(20, 260, 'which ended on ' + String(conference.dateTimeEnd).substring(0,10))
    doc.text(20, 300, 'as participant.')

    doc.save('demo.pdf')
}

    
    return (

        <>
        <div className='user-container'>
            <FaRegUserCircle style={{height:50, width:50, color:'teal', margin:10}}></FaRegUserCircle>
            <h2>{user.firstAndLastName} <span>({user.userAccountRole})</span></h2>      
        </div>
        <Grid container style={{ padding: '5em 0em' }}>
    
            <Grid.Row>
                <Link to="/">
                    <Button color='teal'>HOME</Button>
                </Link>
            </Grid.Row>
            <div className='sticky'>
                <h1>NAPOMENA:</h1>
                <p>Svi događaji se snimaju.</p>
            </div>

            <div className="conference-container">
                <div>
                    <h1 style={{color:"teal"}}>{conference.name}</h1>
                    <hr></hr>
                    <h2><span style={{color:'teal'}}>City:</span></h2>
                    <p style={{marginLeft: 35, fontSize:19}}>{conference.city}</p>
                    <h2><span style={{color:'teal'}}>Description:</span></h2>
                    <p style={{marginLeft: 35, fontSize:19}}>{conference.description}</p>
                    <h2><span style={{color:'teal'}}>Start date:</span></h2>
                    <p style={{marginLeft: 35, fontSize:19}}>{String(conference.dateStart).substring(0,10)}</p>
                    <h2><span style={{color:'teal'}}>End date:</span></h2>
                    <p style={{marginLeft: 35, fontSize:19}}>{String(conference.dateEnd).substring(0,10)}</p>
                    <h2><span style={{color:'teal'}}>Topics:</span></h2>
                    <div>
                        <>
                        {String(conference.topics).split(' ').map((t) => (
                            <p style={{marginLeft: 35, fontSize:19}}>{t}</p>
                        ))}
                        </>
                    </div>
                    
                </div>
                <div>
                    <div className='weather-container'>
                        <BsCloudSunFill style={{height:60, width:60}}></BsCloudSunFill>
                        <p><span style={{color:'teal'}}>Description:</span> {weather.description}</p>
                        <p><span style={{color:'teal'}}>Temperature:</span> {weather.temperature}</p>
                        <p><span style={{color:'teal'}}>Feels like:</span> {weather.feelsLike}</p>
                        <p><span style={{color:'teal'}}>Wind speed:</span> {weather.windSpeed}</p>
                    </div>
                </div>
              </div>

            <Grid.Row>
            {user.userAccountRole === "MAINADMIN" && mandatoryDataGroups===false &&
                <div className='outer'>
                <DataGroupPopup></DataGroupPopup>
                </div>
            } 
            </Grid.Row>
            {mandatoryDataGroups === true &&
            <Grid.Row>
                <div className='data-group-container'>
                    <p className='data-group-element'><b>ADDITIONAL DATA ABOUT CONFERENCE</b></p>  
                    <DataGroupsList/>
                </div>
                {user.userAccountRole==='MAINADMIN' &&
                    <div>
                        <Link to="/dataGroupCreate"><Button color = 'teal' style={{width: 220, margin: 20}}>ADD NEW DATA GROUP</Button></Link>
                    </div>
                }
                
            </Grid.Row>
            }
            
        </Grid>
        <div className='button-container-bottom'>
            <div style={{marginLeft: 70}}>
                <Link to="/specialEvents"><Button color='teal' style={{width: 220}}>CHECK SPECIAL EVENTS</Button></Link>
            </div>
            <div style={{marginLeft: 70}}>
                <Link to="/multimedia"><Button color='teal' style={{width: 220}}>SEE MULTIMEDIA</Button></Link>
            </div>
            {user.userAccountRole==='MAINADMIN' &&
                <div style={{marginLeft: 70}}>
                    <Link to="/showParticipants"><Button color='teal' style={{width:220}}>SHOW PARTICIPANTS</Button></Link>
                    <Link to='/downloadConference'><Button color='teal' style={{width:260, marginLeft:70}}>DOWNLOAD PDF</Button></Link>
                </div>
            }
            {user.userAccountRole==='PARTICIPANT' &&
                <div style={{marginLeft: 70}}>
                    <Link to='/downloadCertificate'><Button color='teal' style={{width:260, marginLeft:70}}>DOWNLOAD CERTIFICATE</Button></Link>
                </div>
            } 
        </div>

        </>
       
        
        
    );
}

