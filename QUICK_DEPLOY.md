# Quick Render Deployment Steps

## Method 1: Using Dockerfile (Recommended - Simpler)

1. **Push to GitHub**
   ```bash
   git add .
   git commit -m "Add Render deployment files"
   git push
   ```

2. **Create Web Service on Render**
   - Go to https://dashboard.render.com/
   - Click "New +" → "Web Service"
   - Connect your GitHub repo
   - Select branch: `main`
   - **Root Directory**: `lms-backend/lms-backend`
   - Render will auto-detect the Dockerfile

3. **Set Environment Variables**
   ```
   SPRING_PROFILES_ACTIVE=prod
   DATABASE_URL=jdbc:mysql://HOST:PORT/DATABASE?user=USER&password=PASS
   JWT_SECRET=your-secret-key-min-32-chars
   ```

4. **Deploy** - Click "Create Web Service"

## Method 2: Using Build Script

If Dockerfile doesn't work, use these settings:
- **Runtime**: Java
- **Build Command**: `./build.sh`
- **Start Command**: `java -Dserver.port=$PORT -jar target/lms-backend-0.0.1-SNAPSHOT.jar`

Before pushing, make build.sh executable:
```bash
git update-index --chmod=+x lms-backend/lms-backend/build.sh
```

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
