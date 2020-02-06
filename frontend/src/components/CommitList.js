import React, {useState, useEffect} from 'react';
import { makeStyles } from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import Divider from '@material-ui/core/Divider';
import ListItemText from '@material-ui/core/ListItemText';
import ListItemAvatar from '@material-ui/core/ListItemAvatar';
import Avatar from '@material-ui/core/Avatar';
import Typography from '@material-ui/core/Typography';
import 'typeface-roboto';
import axios from 'axios';
import checkbox from './../img/checkbox.png';

const useStyles = makeStyles(theme => ({
  root: {
    width: '100%',
    maxWidth: 360,
    backgroundColor: '#F0C808',
    borderRadius: 16,
    border: 2,
    boxShadow: '2px 5px 1px rgba(0, 0, 0, 0.15)'
  },
  inline: {
    display: 'inline',
    color: '#FFF1D0',
    fontFamily: 'Roboto'
  },
  success: {
    color: "red"
  },
  failure: {
    color: "green"
  },
  checkbox: {
    height: 12,
    width: 12,
    paddingRight: 8,
  }
}));

let firstLetterUppercase = (s) => {
  let temp = s.slice(0, 1);
  temp = temp.toUpperCase();
  s = temp + s.slice(1, s.length);

  return s;
}

export default function CommitList() {
  const classes = useStyles();
  const [builds, setState] = useState({});

  useEffect(() => {
    axios.get(process.env.REACT_APP_PROXY + process.env.REACT_APP_URL + "/ci/get")
    .then((res) => {
      setState(res['data'])
    })
    .catch((e) => {
      setState({});
    })

  }, [])

  let data = {
      "1": {
          "log": [
              [
                  "first row:)",
                  "2nd row, more info ye",
                  "Another row?! madness!"
              ],
              [
                  "here comes test rows",
                  "teeeeests",
                  "so coolxd"
              ]
          ],
          "commitSha": "fff",
          "url": "url",
          "status": "success"
      },
      "sickjobid2": {
          "log": [
              [
                  "rowrow",
                  "wow"
              ],
              [
                  "here comes test rows",
                  "teeeeests",
                  "so coolxd"
              ]
          ],
          "commitSha": "shashasha",
          "url": "another url",
          "status": "failure"
      },
      "sickjobid": {
          "log": [
              [
                  "actually insane",
                  "wot",
                  "buildbuildbuildxD"
              ],
              [
                  "here comes test rows",
                  "teeeeests",
                  "so coolxd"
              ]
          ],
          "commitSha": "shasha",
          "url": "urlll",
          "status": "failure"
      }
  }

  return (
    <List className={classes.root}>
      {Object.keys(data).map((key, index) => {
        let divider = index !== Object.keys(data).length - 1 ? <Divider variant="inset" component="li" style={{ marginLeft: 16 }}/> : null;
        return ([<ListItem key={key} alignItems="flex-start">
          <ListItemText
            primary={
            <React.Fragment>
              <Typography
                  component="span"
                  variant="h6"
                  className={classes.inline}
                  color={"textPrimary"}
                  style={{ color: data[key].status.slice(0, 1) === "s" ? "#FFF1D0" : "#DD524F" }}
              >
                  {firstLetterUppercase(data[key].status)}
              </Typography>
            </React.Fragment>
          }
            secondary={
              <React.Fragment>
                <Typography
                  component="span"
                  variant="body2"
                  className={classes.inline}
                  color="textPrimary"
                >
                  {data[key].commitSha}
                </Typography>
                <Typography
                  component="span"
                  variant="body2"
                  className={classes.inline}
                >
                    {" - blablabla javakod"}
                </Typography>
              </React.Fragment>
            }
          />
        </ListItem>, divider])
      })}
    </List>
  );
}