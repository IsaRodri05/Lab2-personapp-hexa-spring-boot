db = db.getSiblingDB("admin");

// Check if user already exists
var existingUser = db.getUser("persona_db");
if (!existingUser) {
  db.createUser({
    user: "persona_db",
    pwd: "persona_db",
    roles: [
      { role: "readWrite", db: "persona_db" },
      { role: "dbAdmin", db: "persona_db" }
    ]
  });
  print("User persona_db created successfully");
} else {
  print("User persona_db already exists");
}

// Switch to application database
db = db.getSiblingDB("persona_db");
// Add any collection initialization here