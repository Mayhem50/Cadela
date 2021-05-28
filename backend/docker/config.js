db = db.getSiblingDB('cadela')
db.createUser(
  { user: 'cadela-user', pwd: 'abc', roles: ['readWrite', 'dbAdmin'] },
  { w: 'majority', wtimeout: 5000 },
)
