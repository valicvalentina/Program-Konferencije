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



  return (
      <PDFViewer style={styles.viewer}>
        <Document>
          <Page size="A4" style={styles.page}>
            <View style={styles.section}>
              <Text style={styles.entityheading}>Participation certificate</Text>
            </View>
            <View style={styles.section}>
              <Text>This certifies that</Text>
            </View>
            <View style={styles.section}>
              <Text>{user.firstAndLastName}</Text>
            </View>
            <View style={styles.section}>
              <Text>{user.country}, {user.companyName}</Text>
            </View>
            <View style={styles.section}>
              <Text>has attended the conference {conference.name}</Text>
            </View>
            <View style={styles.section}>
              <Text>{conference.city}, {conference.description}</Text>
            </View>
            <View style={styles.section}>
              <Text>which ended on {String(conference.dateEnd).substring(0,10)}</Text>
            </View>
            <View style={styles.section}>
              <Text>as participant.</Text>
            </View>
          </Page>
        </Document>
      </PDFViewer>
    );
}

  export default ConferenceDocument;