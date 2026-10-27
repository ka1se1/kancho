import React, { useState, useEffect } from 'react';

// TypeScriptの型定義（Javaのモデルと合わせる）
interface Task {
  id: number;
  title: string;
  category: string;
  isCompleted: boolean;
  requiredMinutes: number;
}

const ScheduleApp: React.FC = () => {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(false);

  // 今日やるべきタスクを取得（本来はAPIからFetch）
  useEffect(() => {
    // モックデータ（実際の開発では fetch('/api/tasks/today') を使用）
    setTasks([
      { id: 1, title: 'SPI 史上最強 (推論)', category: 'SPI', isCompleted: false, requiredMinutes: 60 },
      { id: 2, title: '玉手箱 (図表読み取り)', category: '玉手箱', isCompleted: false, requiredMinutes: 30 },
      { id: 3, title: 'Spring Boot API作成', category: 'Java', isCompleted: false, requiredMinutes: 120 },
    ]);
  }, []);

  // タスクの完了チェック
  const toggleTask = (id: number) => {
    setTasks(tasks.map(task => 
      task.id === id ? { ...task, isCompleted: !task.isCompleted } : task
    ));
    // ここでバックエンドに完了状態を送信する (PUTリクエスト等)
  };

  // 目玉機能：スケジュールの自動調整
  const handleAutoReschedule = async () => {
    setLoading(true);
    try {
      // Spring BootのAPIを叩く
      const response = await fetch('http://localhost:8080/api/tasks/reschedule', {
        method: 'POST',
      });
      
      if (response.ok) {
        alert('未完了のタスクを翌日以降に再配分しました！無理せず頑張りましょう。');
        // タスクリストを再取得して画面を更新
      }
    } catch (error) {
      console.error('エラーが発生しました', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: '20px', maxWidth: '600px', margin: '0 auto', fontFamily: 'sans-serif' }}>
      <h1>🗓 本日の就活×学習ミッション</h1>
      
      <ul style={{ listStyleType: 'none', padding: 0 }}>
        {tasks.map(task => (
          <li key={task.id} style={{ 
            padding: '15px', 
            margin: '10px 0', 
            backgroundColor: task.isCompleted ? '#d4edda' : '#f8f9fa',
            border: '1px solid #dee2e6',
            borderRadius: '8px',
            display: 'flex',
            justifyContent: 'space-between'
          }}>
            <div>
              <span style={{ fontWeight: 'bold', marginRight: '10px' }}>[{task.category}]</span>
              <span style={{ textDecoration: task.isCompleted ? 'line-through' : 'none' }}>
                {task.title} ({task.requiredMinutes}分)
              </span>
            </div>
            <input 
              type="checkbox" 
              checked={task.isCompleted} 
              onChange={() => toggleTask(task.id)} 
              style={{ transform: 'scale(1.5)' }}
            />
          </li>
        ))}
      </ul>

      <div style={{ marginTop: '30px', textAlign: 'center' }}>
        <p style={{ fontSize: '0.9em', color: '#666' }}>
          予定通り終わらなくても大丈夫。AIが明日以降の予定を再構築します。
        </p>
        <button 
          onClick={handleAutoReschedule}
          disabled={loading}
          style={{
            padding: '12px 24px',
            backgroundColor: '#007bff',
            color: 'white',
            border: 'none',
            borderRadius: '5px',
            fontSize: '1em',
            cursor: 'pointer'
          }}
        >
          {loading ? '調整中...' : '🔄 遅れを自動調整する (Reschedule)'}
        </button>
      </div>
    </div>
  );
};

export default ScheduleApp;