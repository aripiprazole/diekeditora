import {graphql} from 'react-relay';

export default graphql`
  query MangaQuery($uid: UniqueId!) {
    manga(uid: $uid) {
      advisory
      competing
      createdAt
      updatedAt
      deletedAt
      title
      uid
      authors {
        displayName
      }
      genres {
        name
      }
      ratings
    }
  }
`;
