# Quick Render Deployment Steps

## Using Dockerfile (Recommended - Most Reliable)

### Step 1: Push to GitHub
```bash
git add .
git commit -m "Add Render deployment files"
git push
```

### Step 2: Create Web Service on Render
1. Go to https://dashboard.render.com/
2. Click "New +" → "Web Service"
3. Connect your GitHub repository
4. Select your repository and branch (usually `main`)

### Step 3: Configure Service
- **Name**: lms-backend (or your preferred name)
- **Region**: Choose closest to your users
- **Branch**: main
- **Root Directory**: `lms-backend/lms-backend`
- **Environment**: Docker
- Render will auto-detect the Dockerfile ✓

### Step 4: Set Environment Variables
Click "Advanced" and add these environment variables:

```
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:mysql://HOST:PORT/DATABASE?user=USER&password=PASS
JWT_SECRET=your-secret-key-min-32-chars
```

### Step 5: Deploy
Click "Create Web Service" and wait for deployment to complete (5-10 minutes)

---

## Alternative: Using Maven Build (If Docker fails)

### Configure these settings on Render:
- **Runtime**: Java
- **Build Command**: `mvn clean install -DskipTests`
- **Start Command**: `java -Dserver.port=$PORT -jar target/lms-backend-0.0.1-SNAPSHOT.jar`

Note: This requires `system.properties` file with `java.runtime.version=21`

## Required Environment Variables

| Variable | Example Value |
|----------|---------------|
| SPRING_PROFILES_ACTIVE | `prod` |
| DATABASE_URL | `jdbc:mysql://host:3306/db?user=admin&password=pass` |
| JWT_SECRET | Generate: `openssl rand -base64 32` |

## Database Options

**Free MySQL Hosting:**
- PlanetScale: https://planetscale.com/
- Railway: https://railway.app/
- Clever Cloud: https://www.clever-cloud.com/

**Or use Render PostgreSQL:**
- Create PostgreSQL database on Render
- Use internal connection URL
- Already configured in application-prod.properties

## Test Your Deployment

```bash
curl https://your-app.onrender.com/api/auth/health
```

## Common Issues

**"JAVA_HOME not defined"**
→ Ensure `system.properties` file exists with `java.runtime.version=21`

**Build fails**
→ Use Dockerfile method instead

**App won't start**
→ Check all environment variables are set correctly

**Database connection fails**
→ Verify DATABASE_URL format and credentials
