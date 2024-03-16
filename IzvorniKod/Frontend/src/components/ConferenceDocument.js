import { Document, Page, Text, StyleSheet, Font, PDFViewer, View} from '@react-pdf/renderer';
import React from 'react';
import axios from 'axios';

// Register Font
Font.register({
  family: "Roboto",
  src:
    "https://cdnjs.cloudflare.com/ajax/libs/ink/3.1.10/fonts/Roboto/roboto-light-webfont.ttf"
});

// Create style with font-family
const styles = StyleSheet.create({
  section: {
    padding: 10,
    fontSize: 15
  },
  sub_section: {
    marginLeft: 10,
    padding: 10,
    fontSize: 15
  },
  viewer: {
    width: window.innerWidth, //the pdf viewer will take up all of the width and height
    height: window.innerHeight,
  },
  page: {
    fontFamily: "Roboto",
    color: "black",
  },
  entityheading: { fontSize: 26 , textAlign: 'center', marginBottom: 0 },
});

function ConferenceDocument ()  {
  const [conference, setConference] = React.useState([]);
  const [mandatoryDataGroups, setMandatoryDataGroups] = React.useState([]);
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

  var operativeViews = operativeAdmins.map(admin => {
    return(
      <View style={styles.sub_section}>
        <Text>{admin.firstAndLastName}</Text>
      </View>
    )
  })

  var participantView = participants.map(p => {
    return(
      <View style={styles.sub_section}>
        <Text>{p.firstAndLastName}</Text>
      </View>
    )
  })

  var specialEventsView = specialEvents.map(e => {
    return(
      <View style={styles.sub_section}>
        <Text>{e.type}</Text>
      </View>
    )
  })

  var DataGroupsView = dataGroups.map(dg => {
    return(
      <View style={styles.sub_section}>
        <Text>{dg.groupName}</Text>
      </View>
    )
  })

  return (
      <PDFViewer style={styles.viewer}>
        <Document>
          <Page size="A4" style={styles.page}>
            <View style={styles.section}>
              <Text style={styles.entityheading}>{conference.name}</Text>
            </View>
            <View style={styles.section}>
              <Text>Conference: {conference.name}</Text>
            </View>
            <View style={styles.section}>
              <Text>City: {conference.city}</Text>
            </View>
            <View style={styles.section}>
              <Text>Description: {conference.description}</Text>
            </View>
            <View style={styles.section}>
              <Text>Start date: {String(conference.dateStart).substring(0, 10)}</Text>
            </View>
            <View style={styles.section}>
              <Text>End date: {String(conference.dateEnd).substring(0, 10)}</Text>
            </View>
            <View style={styles.section}>
              <Text>Topics: {conference.topics}</Text>
            </View>
            <View style={styles.section}>
              <Text>Conference owner: Jose Mourinho</Text>
            </View>
            <View style={styles.section}>
              <Text>Main admin of the conference: {user.firstAndLastName}</Text>
            </View>
            <View style={styles.section}>
              <Text>Operativ admins of the conference:</Text>
            </View>
            {operativeViews}
            <View style={styles.section}>
              <Text>Participants:</Text>
            </View>
            {participantView}
            <View style={styles.section}>
              <Text>Special events:</Text>
            </View>
            {specialEventsView}
            <View style={styles.section}>
              <Text>Data groups:</Text>
            </View>
            {DataGroupsView}
          </Page>
        </Document>
      </PDFViewer>
    );
}

  export default ConferenceDocument;